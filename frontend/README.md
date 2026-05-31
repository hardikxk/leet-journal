# Frontend (Astro + React)

This frontend module provides a basic SPA shell for Leet Journal, including:

- Project landing page
- Login page GUI for gateway/auth flow

## Prerequisites

- Bun (recommended) or npm

## Run locally

Using Bun:

```bash
bun install
bun run dev
```

Using npm:

```bash
npm install
npm run dev
```

The dev server runs on `http://localhost:5173` by default; the preview server uses `npm run preview` and serves the built `dist/` directory.

## Integration notes

- Login action points to the gateway OAuth2 client endpoint:
  - `http://localhost:9000/oauth2/authorization/proxy-client-oidc`
- The frontend is a standalone SPA that initiates the OAuth2 redirect flow — the authorization server (auth) handles user credentials and tokens.
- Before using the UI end-to-end, start the `gateway` and `auth` services (see root `README.md` and `SETUP.md`). If you run the frontend on a different origin you may need to enable CORS on the gateway during development (or proxy the frontend through the gateway).

## Preview (build) steps

```bash
cd frontend
npm install
npm run build
npm run preview
```

This builds the static assets to `frontend/dist` and serves them for manual review.
