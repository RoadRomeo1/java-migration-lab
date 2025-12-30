package com.example.javamigrationlab.entity;

import com.example.common.enums.PersonType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private PersonType type;

    private BigDecimal amount; // Annual Salary/Turnover
    private Integer hoursWorked; // Only for Contractor
    private String profession; // Only for SelfEmployed
    private String businessType; // Only for BusinessOwner
}
