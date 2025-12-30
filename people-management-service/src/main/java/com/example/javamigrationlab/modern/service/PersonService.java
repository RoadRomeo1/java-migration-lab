package com.example.javamigrationlab.modern.service;

import com.example.common.domain.*;
import com.example.javamigrationlab.entity.PersonEntity;
import com.example.javamigrationlab.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person createPerson(Person person) {
        // simulateNetworkLatency();
        PersonEntity entity = mapToEntity(person);
        PersonEntity savedEntity = personRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    private void simulateNetworkLatency() {
        try {
            Thread.sleep(50); // Simulate 50ms DB latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Person getPerson(Long id) {
        return personRepository.findById(id)
                .map(this::mapToDomain)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    public List<Person> getAllPeople() {
        return personRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateMonthlyIncome(Long id) {
        Person person = getPerson(id);
        return switch (person) {
            case FullTimeEmployee(_, _, _, var salary) -> 
                salary.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case Contractor(_, _, _, var rate, var hours) -> 
                rate.multiply(BigDecimal.valueOf(hours));
            case SelfEmployed(_, _, _, var turnover, _) -> 
                turnover.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case BusinessOwner(_, _, _, var turnover, _) -> 
                turnover.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        };
    }

    private PersonEntity mapToEntity(Person person) {
        PersonEntity entity = new PersonEntity();
        entity.setName(person.name());
        entity.setEmail(person.email());
        entity.setType(person.personType());

        switch (person) {
            case FullTimeEmployee(_, _, _, var salary) -> entity.setAmount(salary);
            case Contractor(_, _, _, var rate, var hours) -> {
                entity.setAmount(rate);
                entity.setHoursWorked(hours);
            }
            case SelfEmployed(_, _, _, var turnover, var profession) -> {
                entity.setAmount(turnover);
                entity.setProfession(profession);
            }
            case BusinessOwner(_, _, _, var turnover, var businessType) -> {
                entity.setAmount(turnover);
                entity.setBusinessType(businessType);
            }
        }
        return entity;
    }

    private Person mapToDomain(PersonEntity entity) {
        return switch (entity.getType()) {
            case EMPLOYEE_FULL_TIME -> new FullTimeEmployee(
                    entity.getId(), entity.getName(), entity.getEmail(), entity.getAmount());
            case EMPLOYEE_CONTRACTOR -> new Contractor(
                    entity.getId(), entity.getName(), entity.getEmail(), entity.getAmount(), entity.getHoursWorked());
            case SELF_EMPLOYED -> new SelfEmployed(
                    entity.getId(), entity.getName(), entity.getEmail(), entity.getAmount(), entity.getProfession());
            case BUSINESS_OWNER -> new BusinessOwner(
                    entity.getId(), entity.getName(), entity.getEmail(), entity.getAmount(), entity.getBusinessType());
        };
    }
}
