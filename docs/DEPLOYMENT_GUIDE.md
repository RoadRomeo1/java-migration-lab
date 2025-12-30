# Deployment & Environment Guide

This document outlines how to configure and run the People & Tax Ecosystem across different environments. The system is designed using **Externalized Configuration** principles, making it cloud-native ready.

## 1. Environment Configurations

The system supports multiple profiles and override mechanisms.

### Local Development (IDE)
- **Profile**: `default`
- **Database**: H2 In-Memory (for People Service)
- **Service URLs**: Defaults to `localhost`
- **Steps**:
  1. Run `PeopleManagementApplication` (Port 8080)
  2. Run `TaxEngineApplication` (Port 8081)

### Docker (Local Infrastructure)
- **Profile**: `docker`
- **Database**: PostgreSQL (Containerized)
- **Service URLs**: Uses Docker internal DNS (`http://people-service:8080`)
- **Steps**:
  ```bash
  cd docker
  docker-compose up -d --build
  ```

### Production / Cloud
- **Profile**: `prod` (or any custom cloud profile)
- **Database**: External PostgreSQL/Managed DB
- **Service URLs**: Injected via Environment Variables.

---

## 2. Dynamic Overrides (The 12-Factor Way)

You do **not** need to change code to move to a new environment. Use Environment Variables to override properties at runtime.

### Key Configuration Properties

| Property Name | Env Variable Equivalent | Default | Description |
| :--- | :--- | :--- | :--- |
| `server.port` | `SERVER_PORT` | Varies | Port the service listens on |
| `spring.profiles.active` | `SPRING_PROFILES_ACTIVE` | `default` | Active Spring profile |
| `app.services.people-service.url` | `APP_SERVICES_PEOPLE_SERVICE_URL` | `http://localhost:8080` | URL of the People Service (used by Tax Engine) |
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | - | DB Connection String |

### Example: Running Tax Engine on a custom Prod URL
```bash
java -DAPP_SERVICES_PEOPLE_SERVICE_URL=http://api.production.internal -jar tax-engine-service.jar
```

---

## 3. Best Practices for New Environments

1. **Never Hardcode**: Always use the `${PROPERTY:DEFAULT}` syntax in Java/YAML.
2. **Profile Specific Files**: Use `application-{profile}.properties` for environment-specific constants (like DB dialects).
3. **Secret Management**: For production, use vault or environment secrets for `SPRING_DATASOURCE_PASSWORD`.
4. **Virtual Threads**: Ensure `SPRING_THREADS_VIRTUAL_ENABLED=true` is set to leverage Java 21 performance in all environments.
