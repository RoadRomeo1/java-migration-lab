package com.example.common.domain;

import com.example.common.enums.PersonType;
import java.math.BigDecimal;

/**
 * Represents owners of small/medium businesses (Retailers, Trading).
 * Often eligible for Section 44AD (Presumptive Taxation).
 */
public record BusinessOwner(
        Long id,
        String name,
        String email,
        BigDecimal annualBusinessTurnover,
        String businessType) implements Person {
    @Override
    public PersonType personType() {
        return PersonType.BUSINESS_OWNER;
    }

    @Override
    public BigDecimal income() {
        return annualBusinessTurnover;
    }
}
