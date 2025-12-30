package com.example.tax.strategy;

import com.example.common.enums.TaxRegime;
import com.example.tax.constants.TaxConstants;
import com.example.tax.model.TaxSlab;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class OldRegimeStrategy implements TaxRegimeStrategy {

    private static final List<TaxSlab> SLABS = List.of(
            new TaxSlab(TaxConstants.OldRegime.SLAB_1_LIMIT, TaxConstants.OldRegime.SLAB_2_LIMIT,
                    TaxConstants.OldRegime.RATE_5_PERCENT),
            new TaxSlab(TaxConstants.OldRegime.SLAB_2_LIMIT, TaxConstants.OldRegime.SLAB_3_LIMIT,
                    TaxConstants.OldRegime.RATE_20_PERCENT),
            new TaxSlab(TaxConstants.OldRegime.SLAB_3_LIMIT, TaxConstants.INFINITE_LIMIT,
                    TaxConstants.OldRegime.RATE_30_PERCENT));

    @Override
    public TaxRegime getRegime() {
        return TaxRegime.OLD;
    }

    @Override
    public BigDecimal calculateBaseTax(BigDecimal taxableIncome) {
        BigDecimal totalTax = SLABS.stream()
                .map(slab -> slab.calculate(taxableIncome))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getStandardDeduction() {
        return TaxConstants.OldRegime.STANDARD_DEDUCTION;
    }

    @Override
    public List<TaxSlab> getSlabs() {
        return SLABS;
    }
}
