# Java 17 Migration Practice

This project is a hands-on practice application designed to demonstrate the migration of a Java 8 application to Java 17. It contains two parallel implementations of an Employee Management System: one using legacy Java 8 patterns and another leveraging modern Java 17 features.

## Project Structure

The codebase is divided into two main packages:

-   **`com.example.javamigrationlab.legacy`**: Represents the "Before" state (Java 8).
    -   Uses standard POJOs, explicit casting, and traditional switch statements.
-   **`com.example.javamigrationlab.modern`**: Represents the "After" state (Java 17).
    -   **Sealed Interfaces**: `Employee` interface permits specific implementations.
    -   **Records**: Concise data carriers for `FullTimeEmployee` and `Contractor`.
    -   **Pattern Matching**: Simplified `instanceof` checks.
    -   **Switch Expressions**: More readable and less error-prone logic.

## Prerequisites

-   Java 17 or higher
-   Maven 3.6+

## Running the Application

1.  **Build the project**:
    ```bash
    mvn clean install
    ```

2.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```
    The application will start on `http://localhost:8080`.

## API Endpoints

### Modern Endpoints (Java 17)
| Method | URL | Description |
| :--- | :--- | :--- |
| `POST` | `/employees` | Create a new employee (Full Time or Contractor) |
| `GET` | `/employees/{id}` | Get employee details |
| `GET` | `/employees` | List all employees |
| `GET` | `/employees/{id}/pay` | Calculate pay using modern logic |

### Legacy Endpoints (Java 8)
| Method | URL | Description |
| :--- | :--- | :--- |
| `POST` | `/legacy/employees` | Create a new employee (Legacy) |
| `GET` | `/legacy/employees/{id}` | Get legacy employee details |
| `GET` | `/legacy/employees` | List all legacy employees |
| `GET` | `/legacy/employees/{id}/pay` | Calculate pay using legacy logic |

## Testing

### Unit Tests
```bash
mvn test
```

### Performance Tests
See `src/test/performance/README.md` for detailed instructions.

```bash
cd src/test/performance/scripts
k6 run load-test.js
k6 run spike-test.js
```

## TODO

- [ ] **Jenkins CI/CD Setup**
  - Install Jenkins locally
  - Create pipeline job for on-demand test execution
  - Configure jobs for:
    - Unit tests (`mvn test`)
    - Performance tests (k6 scripts)
    - Build verification (`mvn clean install`)

- [ ] **Integration Testing**
  - Set up integration test suite
  - Add end-to-end API tests
  - Configure test containers for database integration tests
  - Add to Jenkins pipeline

- [ ] **Containerization**
  - Create Dockerfile for the application
  - Set up Docker Compose for local development (app + database)
  - Create Kubernetes manifests (Deployment, Service, ConfigMap)
  - Configure health checks and resource limits
  - Document container deployment process

- [ ] **RDBMS Flexibility**
  - Abstract database configuration using Spring profiles
  - Add support for multiple databases:
    - PostgreSQL
    - MySQL
    - Oracle
    - SQL Server
  - Create profile-specific `application-{db}.properties` files
  - Update schema to use JPA annotations compatible with all RDBMS
  - Add Flyway/Liquibase for database migrations
  - Document database setup for each supported RDBMS

## Resources
-   [Java 17 Language Updates](https://docs.oracle.com/en/java/javase/17/language/index.html)
-   [Spring Boot 3 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
