package org.example;

import org.example.dto.EnergyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(EnergyProperties.class)
public class EnergyRegistryApp {
    static void main(String[] args) {
        SpringApplication.run(EnergyRegistryApp.class, args);
    }
}