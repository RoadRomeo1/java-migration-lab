package com.example.common.domain;

import com.example.common.enums.PersonType;
import java.math.BigDecimal;

public record FullTimeEmployee(
        Long id,
        String name,
        String email,
        BigDecimal annualSalary) implements Person {
    @Override
    public PersonType personType() {
        return PersonType.EMPLOYEE_FULL_TIME;
    }

    @Override
    public BigDecimal income() {
        return annualSalary;
    }
}
