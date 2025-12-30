package com.example.tax.strategy;

import com.example.common.enums.TaxRegime;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TaxStrategyFactory {

    private final Map<TaxRegime, TaxRegimeStrategy> strategies;

    public TaxStrategyFactory(List<TaxRegimeStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(TaxRegimeStrategy::getRegime, Function.identity()));
    }

    public TaxRegimeStrategy getStrategy(TaxRegime regime) {
        TaxRegimeStrategy strategy = strategies.get(regime);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy implemented for regime: " + regime);
        }
        return strategy;
    }
}
