package com.example.tax.constants;

import java.math.BigDecimal;

/**
 * Centralized Repository for Indian Tax Law Constants (FY 2024-25).
 * All hardcoded figures affecting calculations should reside here.
 */
public final class TaxConstants {

    private TaxConstants() {
    } // Prevent instantiation

    // General Constants
    public static final BigDecimal HEALTH_AND_EDU_CESS_RATE = new BigDecimal("0.04");
    public static final BigDecimal INFINITE_LIMIT = new BigDecimal("999999999999");

    /**
     * Presumptive Taxation Rates
     */
    public static final class Presumptive {
        public static final BigDecimal SEC_44ADA_PROFESSIONAL_RATE = new BigDecimal("0.50");
        public static final BigDecimal SEC_44AD_BUSINESS_DIGITAL_RATE = new BigDecimal("0.06");
        public static final BigDecimal SEC_44AD_BUSINESS_CASH_RATE = new BigDecimal("0.08");
    }

    /**
     * New Tax Regime (FY 2024-25)
     */
    public static final class NewRegime {
        public static final BigDecimal STANDARD_DEDUCTION = new BigDecimal("75000");
        public static final BigDecimal REBATE_LIMIT_SEC_87A = new BigDecimal("700000");

        // Slab Thresholds
        public static final BigDecimal SLAB_1_LIMIT = new BigDecimal("300000");
        public static final BigDecimal SLAB_2_LIMIT = new BigDecimal("700000");
        public static final BigDecimal SLAB_3_LIMIT = new BigDecimal("1000000");
        public static final BigDecimal SLAB_4_LIMIT = new BigDecimal("1200000");
        public static final BigDecimal SLAB_5_LIMIT = new BigDecimal("1500000");

        // Slab Rates
        public static final BigDecimal RATE_5_PERCENT = new BigDecimal("0.05");
        public static final BigDecimal RATE_10_PERCENT = new BigDecimal("0.10");
        public static final BigDecimal RATE_15_PERCENT = new BigDecimal("0.15");
        public static final BigDecimal RATE_20_PERCENT = new BigDecimal("0.20");
        public static final BigDecimal RATE_30_PERCENT = new BigDecimal("0.30");
    }

    /**
     * Old Tax Regime
     */
    public static final class OldRegime {
        public static final BigDecimal STANDARD_DEDUCTION = new BigDecimal("50000");

        // Slab Thresholds
        public static final BigDecimal SLAB_1_LIMIT = new BigDecimal("250000");
        public static final BigDecimal SLAB_2_LIMIT = new BigDecimal("500000");
        public static final BigDecimal SLAB_3_LIMIT = new BigDecimal("1000000");

        // Slab Rates
        public static final BigDecimal RATE_5_PERCENT = new BigDecimal("0.05");
        public static final BigDecimal RATE_20_PERCENT = new BigDecimal("0.20");
        public static final BigDecimal RATE_30_PERCENT = new BigDecimal("0.30");
    }
}
