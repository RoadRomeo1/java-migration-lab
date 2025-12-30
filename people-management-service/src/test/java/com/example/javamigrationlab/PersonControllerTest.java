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
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateAndGetFullTimeEmployee() throws Exception {
        String fullTimeEmployeeJson = """
                {
                    "personType": "EMPLOYEE_FULL_TIME",
                    "name": "Alice",
                    "email": "alice@example.com",
                    "annualSalary": 120000.0
                }
                """;

        mockMvc.perform(post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fullTimeEmployeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.personType").value("EMPLOYEE_FULL_TIME"));
    }

    @Test
    void testCreateAndGetContractor() throws Exception {
        String contractorJson = """
                {
                    "personType": "EMPLOYEE_CONTRACTOR",
                    "name": "Bob",
                    "email": "bob@example.com",
                    "hourlyRate": 50.0,
                    "hoursWorked": 160
                }
                """;

        mockMvc.perform(post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contractorJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.personType").value("EMPLOYEE_CONTRACTOR"));
    }

    @Test
    void testCalculateIncome() throws Exception {
        String fullTimeEmployeeJson = """
                {
                    "personType": "EMPLOYEE_FULL_TIME",
                    "name": "Charlie",
                    "email": "charlie@example.com",
                    "annualSalary": 120000.0
                }
                """;

        var result = mockMvc.perform(post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fullTimeEmployeeJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Integer id = com.jayway.jsonpath.JsonPath.read(responseContent, "$.id");

        mockMvc.perform(get("/people/" + id + "/income"))
                .andExpect(status().isOk())
                .andExpect(content().string("10000.00"));
    }
}
