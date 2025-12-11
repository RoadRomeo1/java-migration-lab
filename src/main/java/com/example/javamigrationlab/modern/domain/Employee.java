package com.example.javamigrationlab.modern.domain;

import com.example.javamigrationlab.enums.EmployeeType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FullTimeEmployee.class, name = "FULL_TIME"),
        @JsonSubTypes.Type(value = Contractor.class, name = "CONTRACTOR")
})
public sealed interface Employee permits FullTimeEmployee, Contractor {
    String name();
    String email();
    EmployeeType type();
}
