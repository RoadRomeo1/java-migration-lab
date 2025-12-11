package com.example.javamigrationlab.modern.domain;

import java.math.BigDecimal;

import com.example.javamigrationlab.enums.EmployeeType;

public record FullTimeEmployee(Long id, String name, String email, BigDecimal annualSalary) implements Employee {
    @Override
    public EmployeeType type() {
        return EmployeeType.FULL_TIME;
    }
}
