package com.example.tax.strategy;

import com.example.common.enums.TaxRegime;
import com.example.tax.constants.TaxConstants;
import com.example.tax.model.TaxSlab;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class NewRegimeStrategy implements TaxRegimeStrategy {

    private static final List<TaxSlab> SLABS = List.of(
            new TaxSlab(TaxConstants.NewRegime.SLAB_1_LIMIT, TaxConstants.NewRegime.SLAB_2_LIMIT,
                    TaxConstants.NewRegime.RATE_5_PERCENT),
            new TaxSlab(TaxConstants.NewRegime.SLAB_2_LIMIT, TaxConstants.NewRegime.SLAB_3_LIMIT,
                    TaxConstants.NewRegime.RATE_10_PERCENT),
            new TaxSlab(TaxConstants.NewRegime.SLAB_3_LIMIT, TaxConstants.NewRegime.SLAB_4_LIMIT,
                    TaxConstants.NewRegime.RATE_15_PERCENT),
            new TaxSlab(TaxConstants.NewRegime.SLAB_4_LIMIT, TaxConstants.NewRegime.SLAB_5_LIMIT,
                    TaxConstants.NewRegime.RATE_20_PERCENT),
            new TaxSlab(TaxConstants.NewRegime.SLAB_5_LIMIT, TaxConstants.INFINITE_LIMIT,
                    TaxConstants.NewRegime.RATE_30_PERCENT));

    @Override
    public TaxRegime getRegime() {
        return TaxRegime.NEW;
    }

    @Override
    public BigDecimal calculateBaseTax(BigDecimal taxableIncome) {
        if (taxableIncome.compareTo(TaxConstants.NewRegime.REBATE_LIMIT_SEC_87A) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalTax = SLABS.stream()
                .map(slab -> slab.calculate(taxableIncome))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getStandardDeduction() {
        return TaxConstants.NewRegime.STANDARD_DEDUCTION;
    }

    @Override
    public List<TaxSlab> getSlabs() {
        return SLABS;
    }
}
