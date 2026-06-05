package com.hardik.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Idempotency filter that prevents duplicate form submissions.
 *
 * <p>Clients must include an {@code Idempotency-Key} header with every
 * mutating request (POST, PUT, PATCH, DELETE). The first request with a
 * given key is processed normally. Any subsequent request carrying the
 * same key while the first is still in-flight receives a 409 Conflict
 * immediately, and once the first has completed any retry within the TTL
 * window also receives 409 so the client knows the action already
 * succeeded.</p>
 *
 * <p>Keys are kept in memory and automatically evicted after
 * {@value #KEY_TTL_SECONDS} seconds. In a multi-instance deployment,
 * replace the {@code ConcurrentHashMap} with a shared store such as
 * Redis.</p>
 *
 * <h2>Client-side contract</h2>
 * <pre>
 * POST /register
 * Idempotency-Key: &lt;uuid-generated-once-per-submit-click&gt;
 * </pre>
 * The key should be generated fresh each time the user intends a new
 * submission (e.g. on button click, before the request fires), NOT on
 * every retry of the same submission.
 */
@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    /** Mutating HTTP methods that require idempotency protection. */
    private static final Set<String> PROTECTED_METHODS = Set.of(
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.DELETE.name()
    );

    /** How long (in seconds) a key is remembered after the request finishes. */
    private static final long KEY_TTL_SECONDS = 30;

    /** Header name the client must supply. */
    public static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";

    /**
     * Tracks keys that are currently in-flight or recently completed.
     * Value = System.currentTimeMillis() when the key was first seen.
     */
    private final ConcurrentHashMap<String, Long> seenKeys = new ConcurrentHashMap<>();

    private final ScheduledExecutorService cleaner =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "idempotency-key-cleaner");
                t.setDaemon(true);
                return t;
            });

    public IdempotencyFilter() {
        // Periodically remove keys that have outlived their TTL.
        cleaner.scheduleAtFixedRate(this::evictExpiredKeys,
                KEY_TTL_SECONDS, KEY_TTL_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (!PROTECTED_METHODS.contains(request.getMethod().toUpperCase())) {
            // Safe methods (GET, HEAD, OPTIONS) pass through unchanged.
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getHeader(IDEMPOTENCY_KEY_HEADER);

        if (key == null || key.isBlank()) {
            // No key supplied — let the request through without protection.
            // You may choose to make this mandatory by returning 400 instead.
            filterChain.doFilter(request, response);
            return;
        }

        // putIfAbsent returns null when the key was NOT present → first request.
        Long existing = seenKeys.putIfAbsent(key, System.currentTimeMillis());

        if (existing != null) {
            // A request with this key has already been seen (in-flight or done).
            response.setStatus(HttpStatus.CONFLICT.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"duplicate_request\"," +
                    "\"message\":\"A request with this Idempotency-Key is already being processed or was recently completed. " +
                    "Generate a new key for a fresh submission.\"}"
            );
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Update timestamp to "completed-at" so the TTL clock restarts
            // from when the request finished, not when it started.
            seenKeys.put(key, System.currentTimeMillis());
        }
    }

    private void evictExpiredKeys() {
        long cutoff = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(KEY_TTL_SECONDS);
        seenKeys.entrySet().removeIf(e -> e.getValue() < cutoff);
    }
}
