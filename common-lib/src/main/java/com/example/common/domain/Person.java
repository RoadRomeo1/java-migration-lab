package com.example.common.domain;

import com.example.common.enums.PersonType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "personType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FullTimeEmployee.class, name = "EMPLOYEE_FULL_TIME"),
        @JsonSubTypes.Type(value = Contractor.class, name = "EMPLOYEE_CONTRACTOR"),
        @JsonSubTypes.Type(value = SelfEmployed.class, name = "SELF_EMPLOYED"),
        @JsonSubTypes.Type(value = BusinessOwner.class, name = "BUSINESS_OWNER")
})
public sealed interface Person permits FullTimeEmployee, Contractor, SelfEmployed, BusinessOwner {
    Long id();

    String name();

    String email();

    PersonType personType();

    BigDecimal income();
}
