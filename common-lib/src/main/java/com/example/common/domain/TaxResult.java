package com.example.common.domain;

import java.math.BigDecimal;

public record TaxResult(
        BigDecimal grossIncome,
        BigDecimal deductions,
        BigDecimal taxableIncome,
        BigDecimal baseTax,
        BigDecimal surcharge,
        BigDecimal cess,
        BigDecimal totalTaxLiability,
        BigDecimal netTakeHome) {
}
