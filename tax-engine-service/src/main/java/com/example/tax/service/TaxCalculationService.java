package com.example.tax.service;

import com.example.common.domain.*;
import com.example.common.enums.TaxRegime;
import com.example.tax.constants.TaxConstants;
import com.example.tax.strategy.TaxRegimeStrategy;
import com.example.tax.strategy.TaxStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Smart Tax Calculation Orchestrator.
 * USES Strategy Pattern and Java 21 Record Patterns for maximum flexibility.
 */
@Slf4j
@Service
public class TaxCalculationService {

    private final TaxStrategyFactory strategyFactory;

    public TaxCalculationService(TaxStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public TaxResult calculateTax(Person person, TaxRegime regime) {
        if (log.isInfoEnabled()) {
            log.info("Calculating tax for person ID: {}", person.id());
        }
        TaxRegimeStrategy strategy = strategyFactory.getStrategy(regime);

        BigDecimal grossIncome = person.income();
        BigDecimal deductions = calculateDeductions(person, strategy);
        BigDecimal taxableIncome = grossIncome.subtract(deductions).max(BigDecimal.ZERO);

        BigDecimal baseTax = strategy.calculateBaseTax(taxableIncome);

        BigDecimal cess = baseTax.multiply(TaxConstants.HEALTH_AND_EDU_CESS_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalTax = baseTax.add(cess);
        BigDecimal netTakeHome = grossIncome.subtract(totalTax);

        return new TaxResult(
                grossIncome,
                deductions,
                taxableIncome,
                baseTax,
                BigDecimal.ZERO, // Surcharge logic can be added as another strategy
                cess,
                totalTax,
                netTakeHome);
    }

    /**
     * Persona-based deduction logic using Java 21 Record Patterns.
     * Easily extensible for new Persona types.
     */
    private BigDecimal calculateDeductions(Person person, TaxRegimeStrategy strategy) {
        return switch (person) {
            // Employees get Standard Deduction from the strategy
            case FullTimeEmployee _ -> strategy.getStandardDeduction();
            
            // Contractors usually don't get standard deductions unless specialized
            case Contractor _ -> BigDecimal.ZERO; 
            
            // Professionals: Sec 44ADA (50% is Income, so 50% is Deduction as expenses)
            case SelfEmployed(_, _, _, BigDecimal annualTurnover, _) -> 
                annualTurnover.multiply(BigDecimal.ONE.subtract(TaxConstants.Presumptive.SEC_44ADA_PROFESSIONAL_RATE))
                        .setScale(2, RoundingMode.HALF_UP);
            
            // Businesses: Sec 44AD (6% is Income, so 94% is Deduction as expenses)
            case BusinessOwner(_, _, _, BigDecimal annualBusinessTurnover, _) -> 
                annualBusinessTurnover.multiply(BigDecimal.ONE.subtract(TaxConstants.Presumptive.SEC_44AD_BUSINESS_DIGITAL_RATE))
                        .setScale(2, RoundingMode.HALF_UP);
        };
    }
}
