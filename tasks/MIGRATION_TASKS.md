# Java 21 Migration Checklist

## 1. Project Configuration

- [x] **Update `pom.xml`**
  - Change `<java.version>17</java.version>` to `<java.version>21</java.version>`.
  - Ensure you are using Spring Boot 3.2.x or higher (which supports Java 21).

## 2. Java 21 Features to Implement

- [x] **Record Patterns**

  - Refactor `instanceof` checks in `LegacyEmployeeService.java` (or modern service if applicable).
  - _Goal_: Deconstruct records directly in the `if` statement.
  - _Example_:
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

  - _Status_: Reviewed. No immediate use cases found (no direct `get(0)` or `get(size-1)` calls), but available for future use.

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
  - _Why_: It's more idiomatic, concise, and the compiler can check for exhaustiveness (if the interface is sealed).
  - _Target_: `calculatePay` and `mapToEntity` methods.
  - _Example_:
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
  - _Action_: Add `-XX:+UseZGC -XX:+ZGenerational` to your JVM run arguments.

- [x] **Explore Unnamed Patterns (Preview Feature)**

  - _Note_: This requires enabling preview features (`--enable-preview`).
  - You have unused variables in your patterns (e.g., `id`, `name`, `email` in `calculatePay`).
  - You can replace them with `_` to clearly indicate they are unused.
  - _Example_: `case FullTimeEmployee(_, _, _, var salary)`

- [x] **Use BigDecimal for Monetary Values**

  - _Observation_: Currently `Double` is used for `salary` and `hourlyRate`.
  - _Recommendation_: Use `java.math.BigDecimal` for financial calculations to avoid floating-point precision errors.
  - _Target_: `EmployeeEntity`, `Employee` records, and `EmployeeService`.

- [x] **Add Test Coverage for Pay Calculation**

  - _Observation_: `EmployeeControllerTest` covers creation and retrieval, but not the `calculatePay` logic.
  - _Recommendation_: Add a test case for `GET /employees/{id}/pay` to verify the new `BigDecimal` logic.

- [x] **Consider String Templates (Preview)**
  - _Note_: Since preview features are enabled, you could use String Templates for cleaner string concatenation.
  - _Example_: `STR."Employee not found with id: \{id}"` instead of `"Employee not found with id: " + id`.

## 5. Modern Java 21 Features Showcase (Planned)

These tasks are focused on **demonstrating** Java 21 features in small, targeted places in the app, rather than rewriting everything.

- [ ] **Pattern Matching for `switch` with Record Patterns**

  - _Goal_: Use record patterns directly inside a `switch` over the sealed `Employee` hierarchy.
  - _Where_: Modern `EmployeeService` (e.g., `calculatePay` or an internal mapping method).
  - _Example_:
    ```java
    // Example: calculate pay using switch + record patterns
    BigDecimal calculatePay(Employee employee) {
        return switch (employee) {
            case FullTimeEmployee(var id, var name, var email, var salary) ->
                salary;
            case Contractor(_, _, _, var hourlyRate, var hoursWorked) ->
                hourlyRate.multiply(hoursWorked);
        };
    }
    ```

- [ ] **Structured Concurrency with `StructuredTaskScope`**

  - _Goal_: Demonstrate structured concurrency by running a couple of related tasks in parallel and waiting for both.
  - _Where_: Create a small method in a service layer, e.g. `EmployeeInsightsService`, that fetches:
    - employee details, and
    - calculated pay  
      concurrently using virtual threads.
  - _Example_:

    ```java
    EmployeeInsights getInsights(Long employeeId) throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var detailsTask = scope.fork(() -> employeeService.getEmployee(employeeId));
            var payTask = scope.fork(() -> employeeService.calculatePay(employeeId));

            scope.join();           // Wait for both
            scope.throwIfFailed();  // Propagate first failure

            var details = detailsTask.resultNow();
            var pay = payTask.resultNow();
            return new EmployeeInsights(details, pay);
        }
    }
    ```

- [ ] **Scoped Values (Replacing Simple ThreadLocal Use Cases)**

  - _Goal_: Show how to use `ScopedValue` as a modern alternative to `ThreadLocal` for simple contextual data (e.g., correlation ID).
  - _Where_: Create a small utility or filter that sets a correlation ID per request and makes it available in the service layer.
  - _Example_:

    ```java
    public final class RequestContext {
        public static final ScopedValue<String> CORRELATION_ID = ScopedValue.newInstance();
    }

    // In a filter or controller wrapper
    void handleRequest(Runnable handler) {
        String correlationId = UUID.randomUUID().toString();
        ScopedValue.where(RequestContext.CORRELATION_ID, correlationId)
                   .run(handler);
    }

    // In a service
    void logSomething() {
        String correlationId = RequestContext.CORRELATION_ID.orElse("unknown");
        log.info("Processing with correlationId={}", correlationId);
    }
    ```

- [ ] **Sequenced Collections**

  - _Goal_: Use `SequencedCollection` / `SequencedSet` APIs where order matters (first/last elements or reverse views).
  - _Where_: Any place where you return a list of employees and might want to:
    - add an employee at the beginning or end,
    - access the first/last employee,
    - iterate in reverse order.
  - _Example_:

    ```java
    SequencedCollection<Employee> employees = new ArrayList<>();
    employees.addLast(fullTimeEmployee);
    employees.addFirst(contractor);

    Employee first = employees.getFirst();
    Employee last = employees.getLast();
    SequencedCollection<Employee> reversed = employees.reversed();
    ```

- [ ] **Text Blocks for JSON/SQL in Tests**
  - _Goal_: Improve readability of multi-line strings used in tests (JSON payloads, SQL snippets).
  - _Where_: Test code for controllers/services, or any embedded SQL used for demos.
  - _Example_:
    ```java
    String newEmployeeJson = """
        {
          "type": "FULL_TIME",
          "name": "Alice",
          "email": "alice@example.com",
          "annualSalary": 120000
        }
        """;
    ```
