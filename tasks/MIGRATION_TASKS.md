# Java 21 Migration Checklist

## 1. Project Configuration
- [x] **Update `pom.xml`**
  - Change `<java.version>17</java.version>` to `<java.version>21</java.version>`.
  - Ensure you are using Spring Boot 3.2.x or higher (which supports Java 21).

## 2. Java 21 Features to Implement
- [x] **Record Patterns**
  - Refactor `instanceof` checks in `LegacyEmployeeService.java` (or modern service if applicable).
  - *Goal*: Deconstruct records directly in the `if` statement.
  - *Example*:
    ```java
    // Before
    if (employee instanceof FullTimeEmployee ft) {
        return ft.annualSalary();
    }
    // After
    if (employee instanceof FullTimeEmployee(var id, var name, var email, var salary)) {
        return salary;
    }
    ```

- [x] **Sequenced Collections**
  - *Status*: Reviewed. No immediate use cases found (no direct `get(0)` or `get(size-1)` calls), but available for future use.

- [x] **Virtual Threads** (The "Killer Feature" of Java 21)
  - Enable virtual threads in `src/main/resources/application.properties`:
    ```properties
    spring.threads.virtual.enabled=true
    ```
  - This delegates thread management to the JVM rather than the OS, massively improving throughput for I/O-bound apps.

## 3. Verification
- [ ] Run `mvn clean install` to ensure the build passes.
- [ ] Run the app and verify endpoints still work.

## 4. Suggested Changes for Java 17 to 21 Migration
- [x] **Refactor `if-else` to Switch with Pattern Matching**
  - In `EmployeeService.java`, replace the `if-else` chains using `instanceof` with a modern `switch` expression.
  - *Why*: It's more idiomatic, concise, and the compiler can check for exhaustiveness (if the interface is sealed).
  - *Target*: `calculatePay` and `mapToEntity` methods.
  - *Example*:
    ```java
    switch (employee) {
        case FullTimeEmployee(var id, var name, var email, var salary) -> 
            entity.setSalary(salary);
        case Contractor(var id, var name, var email, var rate, var hours) -> {
            entity.setSalary(rate);
            entity.setHoursWorked(hours);
        }
    }
    ```

- [x] **Enable Generational ZGC**
  - If this application is intended for high performance/low latency, consider enabling the new Generational ZGC.
  - *Action*: Add `-XX:+UseZGC -XX:+ZGenerational` to your JVM run arguments.

- [x] **Explore Unnamed Patterns (Preview Feature)**
  - *Note*: This requires enabling preview features (`--enable-preview`).
  - You have unused variables in your patterns (e.g., `id`, `name`, `email` in `calculatePay`).
  - You can replace them with `_` to clearly indicate they are unused.
  - *Example*: `case FullTimeEmployee(_, _, _, var salary)`

- [x] **Use BigDecimal for Monetary Values**
  - *Observation*: Currently `Double` is used for `salary` and `hourlyRate`.
  - *Recommendation*: Use `java.math.BigDecimal` for financial calculations to avoid floating-point precision errors.
  - *Target*: `EmployeeEntity`, `Employee` records, and `EmployeeService`.

- [x] **Add Test Coverage for Pay Calculation**
  - *Observation*: `EmployeeControllerTest` covers creation and retrieval, but not the `calculatePay` logic.
  - *Recommendation*: Add a test case for `GET /employees/{id}/pay` to verify the new `BigDecimal` logic.

- [x] **Consider String Templates (Preview)**
  - *Note*: Since preview features are enabled, you could use String Templates for cleaner string concatenation.
  - *Example*: `STR."Employee not found with id: \{id}"` instead of `"Employee not found with id: " + id`.
