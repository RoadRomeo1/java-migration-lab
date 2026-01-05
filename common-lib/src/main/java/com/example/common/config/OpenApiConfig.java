package com.example.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("People & Tax Ecosystem API")
                        .version("0.0.1-SNAPSHOT")
                        .description("API for managing people and calculating tax in the Indian context.")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
