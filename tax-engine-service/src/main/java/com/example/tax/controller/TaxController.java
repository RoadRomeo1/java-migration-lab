package com.example.tax.controller;

import com.example.common.domain.Person;
import com.example.common.domain.TaxAssessmentRequest;
import com.example.common.domain.TaxResult;
import com.example.common.enums.TaxRegime;
import com.example.tax.client.PeopleClient;
import com.example.tax.service.TaxCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tax")
public class TaxController {

    private final TaxCalculationService taxCalculationService;
    private final PeopleClient peopleClient;

    public TaxController(TaxCalculationService taxCalculationService, PeopleClient peopleClient) {
        this.taxCalculationService = taxCalculationService;
        this.peopleClient = peopleClient;
    }

    @PostMapping("/calculate")
    public ResponseEntity<TaxResult> calculateTax(@RequestBody TaxAssessmentRequest request) {
        TaxResult result = taxCalculationService.calculateTax(request.person(), request.regime());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/calculate/{personId}")
    public ResponseEntity<TaxResult> calculateTaxForPerson(
            @PathVariable Long personId,
            @RequestParam(defaultValue = "NEW") TaxRegime regime) {
        Person person = peopleClient.getPersonById(personId);
        TaxResult result = taxCalculationService.calculateTax(person, regime);
        return ResponseEntity.ok(result);
    }
}
