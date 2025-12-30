package com.example.tax;

import com.example.common.domain.FullTimeEmployee;
import com.example.common.domain.Person;
import com.example.common.enums.TaxRegime;
import com.example.common.domain.TaxAssessmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaxControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should calculate tax for a person via API")
    void shouldCalculateTaxViaApi() throws Exception {
        Person person = new FullTimeEmployee(1L, "Alice", "alice@test.com", new BigDecimal("1000000"));
        TaxAssessmentRequest request = new TaxAssessmentRequest(person, TaxRegime.NEW);

        // 10L - 75K = 9.25L taxable
        // 3-7L (4L@5%) = 20,000
        // 7-9.25L (2.25L@10%) = 22,500
        // Base = 42,500
        // Cess (4%) = 1,700
        // Total = 44,200

        mockMvc.perform(post("/tax/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grossIncome").value(1000000))
                .andExpect(jsonPath("$.totalTaxLiability").value(44200.0));
    }
}
