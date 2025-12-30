package com.example.tax.model;

import java.math.BigDecimal;

/**
 * Represents a single Tax Slab rule.
 */
public record TaxSlab(
        BigDecimal lowLimit,
        BigDecimal highLimit,
        BigDecimal rate) {
    public BigDecimal calculate(BigDecimal income) {
        if (income.compareTo(lowLimit) <= 0)
            return BigDecimal.ZERO;
        BigDecimal taxableInThisSlab = income.min(highLimit).subtract(lowLimit);
        return taxableInThisSlab.multiply(rate);
    }
}
