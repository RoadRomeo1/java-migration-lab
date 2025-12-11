package com.example.javamigrationlab.modern.service;

import com.example.javamigrationlab.modern.domain.Contractor;
import com.example.javamigrationlab.modern.domain.Employee;
import com.example.javamigrationlab.modern.domain.FullTimeEmployee;
import com.example.javamigrationlab.entity.EmployeeEntity;
import com.example.javamigrationlab.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {
        simulateNetworkLatency();
        EmployeeEntity entity = mapToEntity(employee);
        EmployeeEntity savedEntity = employeeRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    private void simulateNetworkLatency() {
        try {
            Thread.sleep(50); // Simulate 50ms DB latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id)
                .map(this::mapToDomain)
                // Java 21 Feature: String Templates
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    // Java 21 Feature: Pattern Matching for instanceof (Standard in 17)
    public BigDecimal calculatePay(Long emp_id) {
        Employee employee = getEmployee(emp_id);
        switch (employee) {
            case FullTimeEmployee(_,_,_, var salary):
                return salary.divide(BigDecimal.valueOf(12), 2, BigDecimal.ROUND_HALF_UP);
            case Contractor(_,_, _, var hourlyRate, var hoursWorked):
                return hourlyRate.multiply(BigDecimal.valueOf(hoursWorked));
            default:
                throw new IllegalArgumentException("Unknown employee type");
        }
    }

    private EmployeeEntity mapToEntity(Employee employee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setName(employee.name());
        entity.setEmail(employee.email());
        entity.setType(employee.type());

        // Java 21 Feature: Pattern Matching for instanceof
        if (employee instanceof FullTimeEmployee(_,_, _, var salary)) {
            entity.setSalary(salary);
        } else if (employee instanceof Contractor(_,_, _, var hourlyRate, var hoursWorked)) {
            entity.setSalary(hourlyRate);
            entity.setHoursWorked(hoursWorked);
        }

        return entity;
    }

    private Employee mapToDomain(EmployeeEntity entity) {
        // Java 17 Feature: Switch Expression with Enum
        return switch (entity.getType()) {
            case FULL_TIME -> new FullTimeEmployee(
                    entity.getId(),
                    entity.getName(),
                    entity.getEmail(),
                    entity.getSalary());
            case CONTRACTOR -> new Contractor(
                    entity.getId(),
                    entity.getName(),
                    entity.getEmail(),
                    entity.getSalary(),
                    entity.getHoursWorked());
        };
    }
}
