package com.example.javamigrationlab;

import com.example.javamigrationlab.modern.domain.Contractor;
import com.example.javamigrationlab.modern.domain.FullTimeEmployee;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for Employee API endpoints.
 * Uses Testcontainers to spin up a real PostgreSQL database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Employee API Integration Tests")
class EmployeeIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @Test
    @DisplayName("Should create and retrieve a full-time employee")
    void shouldCreateAndRetrieveFullTimeEmployee() {
        // Create a full-time employee
        FullTimeEmployee employee = new FullTimeEmployee(
                null,
                "John Doe",
                "john.doe@example.com",
                new BigDecimal("75000.0"));

        Number employeeId = given()
                .contentType(ContentType.JSON)
                .body(employee)
                .when()
                .post("/employees")
                .then()
                .statusCode(201)
                .body("name", equalTo("John Doe"))
                .body("annualSalary", equalTo(75000.0f))
                .body("employeeType", equalTo("FULL_TIME"))
                .extract()
                .path("id");

        // Retrieve the employee
        given()
                .when()
                .get("/employees/{id}", employeeId.longValue())
                .then()
                .statusCode(200)
                .body("id", equalTo(employeeId.intValue()))
                .body("name", equalTo("John Doe"))
                .body("annualSalary", equalTo(75000.0f));
    }

    @Test
    @DisplayName("Should create and retrieve a contractor")
    void shouldCreateAndRetrieveContractor() {
        // Create a contractor
        Contractor contractor = new Contractor(
                null,
                "Jane Smith",
                "jane.smith@example.com",
                new BigDecimal("50"),
                160);

        Number contractorId = given()
                .contentType(ContentType.JSON)
                .body(contractor)
                .when()
                .post("/employees")
                .then()
                .statusCode(201)
                .body("name", equalTo("Jane Smith"))
                .body("hourlyRate", equalTo(50))
                .body("hoursWorked", equalTo(160))
                .body("employeeType", equalTo("CONTRACTOR"))
                .extract()
                .path("id");

        // Retrieve the contractor
        given()
                .when()
                .get("/employees/{id}", contractorId.longValue())
                .then()
                .statusCode(200)
                .body("id", equalTo(contractorId.intValue()))
                .body("name", equalTo("Jane Smith"));
    }

    @Test
    @DisplayName("Should retrieve all employees")
    void shouldRetrieveAllEmployees() {
        // Create multiple employees
        FullTimeEmployee emp1 = new FullTimeEmployee(null, "Alice", "alice@example.com", new BigDecimal("60000"));
        FullTimeEmployee emp2 = new FullTimeEmployee(null, "Bob", "bob@example.com", new BigDecimal("70000"));

        given().contentType(ContentType.JSON).body(emp1).post("/employees");
        given().contentType(ContentType.JSON).body(emp2).post("/employees");

        // Retrieve all employees
        given()
                .when()
                .get("/employees")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(2)))
                .body("name", hasItems("Alice", "Bob"));
    }

    @Test
    @DisplayName("Should calculate pay for full-time employee")
    void shouldCalculatePayForFullTimeEmployee() {
        // Create employee
        FullTimeEmployee employee = new FullTimeEmployee(
                null,
                "Test Employee",
                "test@example.com",
                new BigDecimal("60000"));

        Number employeeId = given()
                .contentType(ContentType.JSON)
                .body(employee)
                .post("/employees")
                .then()
                .extract()
                .path("id");

        // Calculate pay
        given()
                .when()
                .get("/employees/{id}/pay", employeeId.longValue())
                .then()
                .statusCode(200)
                .body(equalTo("5000.00")); // Monthly pay (annual/12)
    }

    @Test
    @DisplayName("Should calculate pay for contractor")
    void shouldCalculatePayForContractor() {
        // Create contractor
        Contractor contractor = new Contractor(
                null,
                "Test Contractor",
                "contractor@example.com",
                new BigDecimal("100"),
                40);

        Number contractorId = given()
                .contentType(ContentType.JSON)
                .body(contractor)
                .post("/employees")
                .then()
                .extract()
                .path("id");

        // Calculate pay (100 * 40 = 4000)
        given()
                .when()
                .get("/employees/{id}/pay", contractorId.longValue())
                .then()
                .statusCode(200)
                .body(equalTo("4000.00"));
    }

    @Test
    @DisplayName("Should return 404 for non-existent employee")
    void shouldReturn404ForNonExistentEmployee() {
        given()
                .when()
                .get("/employees/{id}", 99999)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should handle database persistence correctly")
    void shouldHandleDatabasePersistence() {
        // Create an employee
        FullTimeEmployee employee = new FullTimeEmployee(
                null,
                "Persistence Test",
                "persist@example.com",
                new BigDecimal("55000.0"));

        Number employeeId = given()
                .contentType(ContentType.JSON)
                .body(employee)
                .post("/employees")
                .then()
                .extract()
                .path("id");

        // Verify it persists across multiple requests
        for (int i = 0; i < 3; i++) {
            given()
                    .when()
                .get("/employees/{id}", employeeId.longValue())
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Persistence Test"))
                .body("annualSalary", equalTo(55000.0f));
        }
    }
}
