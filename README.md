## 📚 Project Documentation

To maintain a clean and professional structure, all deep-dive documentation has been centralized in the `/docs` directory:

- [**System Architecture**](./docs/ARCHITECTURAL_BLUEPRINT.md): High-level design and monorepo structure.
- [**Deployment & Environments**](./docs/DEPLOYMENT_GUIDE.md): How to run the app in Local, Docker, and Cloud.
- [**Tax Engine Standards**](./docs/TAX_ENGINE_STANDARDS.md): Logical breakdown of Indian Tax laws and our Strategy implementation.
- [**Quality Standards**](./docs/QUALITY_STANDARDS.md): Guide to Checkstyle, PMD, and SpotBugs integration.
- [**API Test Plan**](./docs/API_TEST_PLAN.md): Manual verification steps and Postman payloads.
- [**Java 21 Migration Guide**](./docs/JAVA_21_MIGRATION_GUIDE.md): Technical checklist of features migrated (Records, Switch Expressions, etc.).


---

## 🚀 Quick Start

### Running with Docker (Full Stack)
```bash
cd docker
docker-compose up -d --build
```
- Tax Engine Service: `http://localhost:8081`
> **Note**: If you face database errors like `database "peopledb" does not exist`, run `docker-compose down -v` to reset the volumes and restart.


### Running Locally (Individual Services)
```bash
mvn clean install -DskipTests
# Service 1
mvn spring-boot:run -pl people-management-service
# Service 2
mvn spring-boot:run -pl tax-engine-service
```

## 🧪 Testing

### Automated Tests
```bash
# Run all tests in the ecosystem
mvn test
```

### Performance Benchmarks
See `people-management-service/src/test/performance/README.md` for k6 load testing instructions.

---

## 🛠️ Roadmap & TODO

Current Phase: **Phase 2 - Intelligent Orchestration** (Completed ✅)

- [ ] **Tax Engine Roadmap**
  - [ ] **Robustness & Testing**: Implement negative cases, edge cases, and stress tests.
  - [ ] **Expanded Tax Rules**: Implement Sections 80C, 80D, 24b and Surcharges.
  - [ ] **Regime Optimization**: Endpoint to suggest optimal regime (Old vs New).
  - [ ] **Internal Research**: Explore `RestClient` vs `OpenFeign`.

- [ ] **Observability & Ops**
  - [x] Add Spring Boot Actuator to all services.
  - [x] Standardize structured logging and Correlation IDs.


- [ ] **Infrastructure**
  - [x] GitHub Actions CI/CD Pipeline integration.
  - [x] Static Code Analysis Setup (SpotBugs, PMD, Checkstyle).
  - [ ] Multi-DB support (PostgreSQL/MySQL toggle).


## Resources

- [Java 17 Language Updates](https://docs.oracle.com/en/java/javase/17/language/index.html)
- [Spring Boot 3 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
