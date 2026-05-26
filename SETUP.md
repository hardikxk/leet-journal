# Project Setup

## Prerequisites

- Java 25
- Maven
- PostgreSQL
- Google GenAI API key

---

## PostgreSQL Setup

Run PostgreSQL locally or using Docker/Podman.

You can also run PostgreSQL using Docker:

```bash
docker run --name leet-journal-db \
-e POSTGRES_DB=db \
-e POSTGRES_USER=user \
-e POSTGRES_PASSWORD=pass \
-p 5432:5432 \
-d postgres
```

Default configuration:

```text
Database: db
Username: user
Password: pass
Port: 5432
```

---

## Google GenAI API Key Setup

1. Visit:

https://aistudio.google.com/app/apikey

2. Sign in with your Google account

3. Create a new API key

4. Add it to environment variables:

```bash
GOOGLE_GENAI_API_KEY=your_api_key
```

---

## Google OAuth Setup

1. Open Google Cloud Console:

https://console.cloud.google.com/

2. Create a new Google Cloud project

3. Enable Google OAuth APIs

4. Configure the OAuth consent screen

5. Create OAuth Client ID and Client Secret

6. Add the authorized redirect URI in Google Cloud Console:

```text
http://127.0.0.1:9000/login/oauth2/code/proxy-client-oidc
```

7. Add the OAuth credentials to environment variables:

```bash
GOOGLE_CLIENT_ID=your_client_id
GOOGLE_CLIENT_SECRET=your_client_secret
```

---

## Running Services

Open a separate terminal for each service before running them.

The `auth` service should be started before the `gateway` service to avoid gateway startup errors.

1. PostgreSQL
2. auth
3. problems-service
4. mail-service
5. ai-service
6. gateway
7. batch-service

Example:

```bash
cd auth
./mvnw spring-boot:run
```