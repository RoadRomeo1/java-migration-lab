package com.example.javamigrationlab.modern.domain;

import java.math.BigDecimal;

import com.example.javamigrationlab.enums.EmployeeType;

public record Contractor(Long id, String name, String email, BigDecimal hourlyRate, Integer hoursWorked)
        implements Employee {
    @Override
    public EmployeeType type() {
        return EmployeeType.CONTRACTOR;
    }
}
