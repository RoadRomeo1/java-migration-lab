package com.example.javamigrationlab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateAndGetFullTimeEmployee() throws Exception {
        // Java 15 Feature: Text Blocks
        String fullTimeEmployeeJson = """
                {
                    "employeeType": "FULL_TIME",
                    "name": "Alice",
                    "email": "alice@example.com",
                    "annualSalary": 120000.0
                }
                """;

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fullTimeEmployeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.employeeType").value("FULL_TIME"));
    }

    @Test
    void testCreateAndGetContractor() throws Exception {
        // Java 15 Feature: Text Blocks
        String contractorJson = """
                {
                    "employeeType": "CONTRACTOR",
                    "name": "Bob",
                    "email": "bob@example.com",
                    "hourlyRate": 50.0,
                    "hoursWorked": 160
                }
                """;

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contractorJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.employeeType").value("CONTRACTOR"));
    }

    @Test
    void testCalculatePay() throws Exception {
        String fullTimeEmployeeJson = """
                {
                    "employeeType": "FULL_TIME",
                    "name": "Charlie",
                    "email": "charlie@example.com",
                    "annualSalary": 120000.0
                }
                """;

        var result = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fullTimeEmployeeJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        // Simple string manipulation to get ID, or use JsonPath if available.
        // Using a regex or simple split for simplicity as we don't have ObjectMapper
        // injected explicitly yet
        // But Spring Boot Test usually has com.jayway.jsonpath
        Integer id = com.jayway.jsonpath.JsonPath.read(responseContent, "$.id");

        mockMvc.perform(get("/employees/" + id + "/pay"))
                .andExpect(status().isOk())
                .andExpect(content().string("10000.00")); // BigDecimal default toString might vary, but let's check.
        // Actually, calculatePay returns BigDecimal.
        // 120000 / 12 = 10000.
        // The scale was set to 2 in the service: .divide(BigDecimal.valueOf(12), 2,
        // BigDecimal.ROUND_HALF_UP);
        // So it should be 10000.00
    }
}
