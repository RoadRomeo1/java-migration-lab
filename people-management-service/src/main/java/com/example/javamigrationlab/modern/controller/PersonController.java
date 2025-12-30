package com.example.javamigrationlab.modern.controller;

import com.example.common.domain.Person;
import com.example.javamigrationlab.modern.service.PersonService;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        return new ResponseEntity<>(personService.createPerson(person), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPerson(id));
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPeople() {
        return ResponseEntity.ok(personService.getAllPeople());
    }

    @GetMapping("/{id}/income")
    public ResponseEntity<BigDecimal> getMonthlyIncome(@PathVariable Long id) {
        return ResponseEntity.ok(personService.calculateMonthlyIncome(id));
    }
}
