package com.example.common.domain;

import com.example.common.enums.PersonType;
import java.math.BigDecimal;

/**
 * Represents professionals like Doctors, Consultants, Freelancers.
 * Often eligible for Section 44ADA (Presumptive Taxation).
 */
public record SelfEmployed(
        Long id,
        String name,
        String email,
        BigDecimal annualTurnover,
        String profession) implements Person {
    @Override
    public PersonType personType() {
        return PersonType.SELF_EMPLOYED;
    }

    @Override
    public BigDecimal income() {
        return annualTurnover;
    }
}
