package com.example.tax.service;

import com.example.common.domain.FullTimeEmployee;
import com.example.common.domain.Person;
import com.example.common.domain.SelfEmployed;
import com.example.common.domain.BusinessOwner;
import com.example.common.enums.TaxRegime;
import com.example.common.domain.TaxResult;
import com.example.tax.strategy.NewRegimeStrategy;
import com.example.tax.strategy.OldRegimeStrategy;
import com.example.tax.strategy.TaxStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxCalculationServiceTest {

    private TaxCalculationService taxCalculationService;

    @BeforeEach
    void setUp() {
        TaxStrategyFactory factory = new TaxStrategyFactory(List.of(
                new NewRegimeStrategy(),
                new OldRegimeStrategy()));
        taxCalculationService = new TaxCalculationService(factory);
    }

    @Test
    @DisplayName("Salaried Person (New Regime) - No tax up to 7.75L")
    void testSalariedNewRegimeNoTax() {
        Person employee = new FullTimeEmployee(1L, "Salaried Person", "sal@test.com", new BigDecimal("775000"));
        TaxResult result = taxCalculationService.calculateTax(employee, TaxRegime.NEW);

        // 7.75L - 75K (Standard Deduction) = 7.0L -> Section 87A Rebate -> 0 Tax
        assertEquals(0, BigDecimal.ZERO.compareTo(result.totalTaxLiability()));
        assertEquals(0, new BigDecimal("775000").compareTo(result.netTakeHome()));
    }

    @Test
    @DisplayName("Salaried Person (New Regime) - High Income tax calculation")
    void testSalariedNewRegimeHighIncome() {
        Person employee = new FullTimeEmployee(1L, "Rich Dev", "rich@test.com", new BigDecimal("2075000"));
        TaxResult result = taxCalculationService.calculateTax(employee, TaxRegime.NEW);

        // 20.75L - 75K = 20L taxable
        // 3-7L (4L) @ 5% = 20,000
        // 7-10L (3L) @ 10% = 30,000
        // 10-12L (2L) @ 15% = 30,000
        // 12-15L (3L) @ 20% = 60,000
        // 15-20L (5L) @ 30% = 1,50,000
        // Total Base = 2,90,000
        // Cess (4%) = 11,600
        // Total = 3,01,600
        assertEquals(new BigDecimal("301600.00"), result.totalTaxLiability());
    }

    @Test
    @DisplayName("Self Employed Professional (44ADA) - 50% Presumptive Tax")
    void testSelfEmployedPresumptiveTax() {
        // Turnover 10L -> 5L taxable
        Person doctor = new SelfEmployed(1L, "Dr. Strange", "strange@test.com", new BigDecimal("1000000"), "Doctor");
        TaxResult result = taxCalculationService.calculateTax(doctor, TaxRegime.OLD);

        // 10L * 0.5 = 5L taxable
        // Old regime slabs: 0-2.5L (0), 2.5-5L (2.5L * 5%) = 12,500
        // Cess (4%) = 500
        // Total = 13,000
        assertEquals(new BigDecimal("13000.00"), result.totalTaxLiability());
    }

    @Test
    @DisplayName("Business Owner (44AD) - 6% Presumptive Tax")
    void testBusinessOwnerPresumptiveTax() {
        // Turnover 50L -> 3L taxable
        Person shopOwner = new BusinessOwner(1L, "Shop Owner", "shop@test.com", new BigDecimal("5000000"), "Retail");
        TaxResult result = taxCalculationService.calculateTax(shopOwner, TaxRegime.NEW);

        // 50L * 0.06 = 3L taxable -> Slab 1 (0-3L) -> 0 tax
        assertEquals(0, BigDecimal.ZERO.compareTo(result.totalTaxLiability()));
    }
}
