package com.example.tax.client;

import com.example.common.domain.Person;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "people-management-service", url = "${app.services.people-service.url:http://localhost:8080}")
public interface PeopleClient {

    @GetMapping("/people/{id}")
    Person getPersonById(@PathVariable("id") Long id);
}
