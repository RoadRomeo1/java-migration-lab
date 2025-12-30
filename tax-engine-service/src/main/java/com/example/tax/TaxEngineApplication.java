package com.example.tax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TaxEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxEngineApplication.class, args);
    }
}
