package com.example.tax.strategy;

import com.example.common.enums.TaxRegime;
import com.example.tax.model.TaxSlab;
import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for Tax Regime calculation logic (Old vs New).
 */
public interface TaxRegimeStrategy {
    TaxRegime getRegime();

    BigDecimal calculateBaseTax(BigDecimal taxableIncome);

    BigDecimal getStandardDeduction();

    List<TaxSlab> getSlabs();
}
