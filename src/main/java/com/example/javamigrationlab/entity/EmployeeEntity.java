package com.example.javamigrationlab.entity;

import com.example.javamigrationlab.enums.EmployeeType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private EmployeeType type;

    private BigDecimal salary; // Annual for FullTime, Hourly Rate for Contractor
    private Integer hoursWorked; // Only for Contractor
}
