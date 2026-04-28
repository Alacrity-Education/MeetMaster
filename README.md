# MeetMaster

A lightweight web app for tracking meeting attendance. Create a meeting, share the QR code on screen, and participants scan it to register their presence.

## Features

- **OIDC authentication** - login via any OpenID Connect provider
- **Meeting list** - filterable by past 7 days, 30 days, or all time
- **QR code** - each meeting detail page generates a scannable QR that links directly to it
- **Participation tracking** - authenticated users join with one tap; the participant list refreshes live
- **Manual add** - meeting creators can add participants by name directly
- **Auto-expiry** - meetings older than 8 hours are automatically marked inactive

The app is configured entirely through environment variables (see `deploy/.env`):

| Variable              | Description                                        | Default      |
|-----------------------|----------------------------------------------------|--------------|
| `PG_HOST`             | PostgreSQL host                                    | `localhost`  |
| `PG_PORT`             | PostgreSQL port                                    | `5432`       |
| `PG_DATABASE`         | Database name                                      | `meetmaster` |
| `PG_USER`             | Database user                                      | `meetmaster` |
| `PG_PASS`             | Database password                                  | `meetmaster` |
| `OIDC_CLIENT_ID`      | OIDC client ID                                     | *(required)* |
| `OIDC_CLIENT_SECRET`  | OIDC client secret                                 | *(required)* |
| `OIDC_AUTH_URL`       | OIDC authorization endpoint URL                    | *(required)* |
| `OIDC_TOKEN_URL`      | OIDC token endpoint URL                            | *(required)* |
| `OIDC_USER_INFO_URL`  | OIDC user info endpoint URL                        | *(required)* |
| `OIDC_JWKS_URL`       | OIDC JWK set endpoint URL                          | *(required)* |
| `NAME_CLAIM`          | JWT claim used as the user's display name          | `name`       |
| `TRUST_PROXY_HEADERS` | Trust `X-Forwarded-*` headers from a reverse proxy | `false`      |
| `ICON_URL`            | URL of the logo shown in the top bar               | *(empty)*    |
| `FAVICON_URL`         | URL of the favicon                                 | *(empty)*    |

## Deployment

Clone the repository, open the `deploy/.env` file and fill in your values, and run:

```bash
docker compose -f deploy/docker-compose.yml up -d
```

Docker images are published to `ghcr.io/alacrity-education/meetmaster`.
