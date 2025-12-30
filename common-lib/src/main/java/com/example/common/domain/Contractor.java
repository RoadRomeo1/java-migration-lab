package com.example.common.domain;

import com.example.common.enums.PersonType;
import java.math.BigDecimal;

public record Contractor(
        Long id,
        String name,
        String email,
        BigDecimal hourlyRate,
        Integer hoursWorked) implements Person {
    @Override
    public PersonType personType() {
        return PersonType.EMPLOYEE_CONTRACTOR;
    }

    @Override
    public BigDecimal income() {
        return hourlyRate.multiply(BigDecimal.valueOf(hoursWorked));
    }
}
