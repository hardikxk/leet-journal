package com.hardik.problemsservice.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class LocalExecutorService {

    private static final Logger logger = Logger.getLogger(LocalExecutorService.class.getName());
    private static final long DEFAULT_TIMEOUT_SECONDS = 30;
    private static final long MAX_TIMEOUT_SECONDS = 120;

    public String executeJava() {
        return executeJavaWithTimeout(DEFAULT_TIMEOUT_SECONDS);
    }

    public String executeJavaWithTimeout(long timeoutSeconds) {
        if (timeoutSeconds > MAX_TIMEOUT_SECONDS) {
            timeoutSeconds = MAX_TIMEOUT_SECONDS;
        }
        return executeJavaCode("BinarySearch.java", timeoutSeconds);
    }

    public String executeJavaCode(String codeFileName, long timeoutSeconds) {
        Path tempFile = null;
        Process process = null;
        try {
            if (timeoutSeconds <= 0 || timeoutSeconds > MAX_TIMEOUT_SECONDS) {
                timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
            }

            Resource classPathResource = new ClassPathResource(codeFileName);
            tempFile = Files.createTempFile("run-", ".java");
            Files.copy(classPathResource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            ProcessBuilder pb = new ProcessBuilder("java", tempFile.toString());
            pb.redirectErrorStream(true);
            process = pb.start();

            boolean completed = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);

            if (!completed) {
                process.destroyForcibly();
                return "Execution timeout: Process exceeded " + timeoutSeconds + " seconds";
            }

            String output = new String(process.getInputStream().readAllBytes());
            return output;

        } catch (Exception e) {
            logger.warning("Execution failed: " + e.getMessage());
            return "Execution Failed: " + e.getMessage();
        } finally {
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
            }
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (Exception e) {
                    logger.warning("Failed to delete temp file: " + e.getMessage());
                }
            }
        }
    }
}
