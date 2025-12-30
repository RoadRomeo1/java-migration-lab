package com.example.javamigrationlab;

import com.example.common.domain.Contractor;
import com.example.common.domain.FullTimeEmployee;
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
 * Integration tests for People API endpoints.
 * Uses Testcontainers to spin up a real PostgreSQL database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("People API Integration Tests")
class PersonIntegrationTest extends BaseIntegrationTest {

        @BeforeEach
        void setUp() {
                RestAssured.port = port;
                RestAssured.basePath = "";
        }

        @Test
        @DisplayName("Should create and retrieve a full-time employee")
        void shouldCreateAndRetrieveFullTimeEmployee() {
                FullTimeEmployee employee = new FullTimeEmployee(
                                null,
                                "John Doe",
                                "john.doe@example.com",
                                new BigDecimal("75000.0"));

                Number personId = given()
                                .contentType(ContentType.JSON)
                                .body(employee)
                                .when()
                                .post("/people")
                                .then()
                                .statusCode(201)
                                .body("name", equalTo("John Doe"))
                                .body("annualSalary", equalTo(75000.0f))
                                .body("personType", equalTo("EMPLOYEE_FULL_TIME"))
                                .extract()
                                .path("id");

                given()
                                .when()
                                .get("/people/{id}", personId.longValue())
                                .then()
                                .statusCode(200)
                                .body("id", equalTo(personId.intValue()))
                                .body("name", equalTo("John Doe"))
                                .body("annualSalary", equalTo(75000.0f));
        }

        @Test
        @DisplayName("Should create and retrieve a contractor")
        void shouldCreateAndRetrieveContractor() {
                Contractor contractor = new Contractor(
                                null,
                                "Jane Smith",
                                "jane.smith@example.com",
                                new BigDecimal("50"),
                                160);

                Number personId = given()
                                .contentType(ContentType.JSON)
                                .body(contractor)
                                .when()
                                .post("/people")
                                .then()
                                .statusCode(201)
                                .body("name", equalTo("Jane Smith"))
                                .body("hourlyRate", equalTo(50))
                                .body("hoursWorked", equalTo(160))
                                .body("personType", equalTo("EMPLOYEE_CONTRACTOR"))
                                .extract()
                                .path("id");

                given()
                                .when()
                                .get("/people/{id}", personId.longValue())
                                .then()
                                .statusCode(200)
                                .body("id", equalTo(personId.intValue()))
                                .body("name", equalTo("Jane Smith"));
        }

        @Test
        @DisplayName("Should retrieve all people")
        void shouldRetrieveAllPeople() {
                FullTimeEmployee emp1 = new FullTimeEmployee(null, "Alice", "alice@example.com",
                                new BigDecimal("60000"));
                FullTimeEmployee emp2 = new FullTimeEmployee(null, "Bob", "bob@example.com", new BigDecimal("70000"));

                given().contentType(ContentType.JSON).body(emp1).post("/people");
                given().contentType(ContentType.JSON).body(emp2).post("/people");

                given()
                                .when()
                                .get("/people")
                                .then()
                                .statusCode(200)
                                .body("$", hasSize(greaterThanOrEqualTo(2)))
                                .body("name", hasItems("Alice", "Bob"));
        }

        @Test
        @DisplayName("Should calculate monthly income")
        void shouldCalculateMonthlyIncome() {
                FullTimeEmployee employee = new FullTimeEmployee(null, "Test Person", "test@example.com",
                                new BigDecimal("60000"));

                Number personId = given()
                                .contentType(ContentType.JSON)
                                .body(employee)
                                .post("/people")
                                .then()
                                .extract()
                                .path("id");

                given()
                                .when()
                                .get("/people/{id}/income", personId.longValue())
                                .then()
                                .statusCode(200)
                                .body(equalTo("5000.00"));
        }

        @Test
        @DisplayName("Should return 404 for non-existent person")
        void shouldReturn404ForNonExistentPerson() {
                given()
                                .when()
                                .get("/people/{id}", 99999)
                                .then()
                                .statusCode(404);
        }
}
