package com.orakuma.rogator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RogatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RogatorApplication.class, args);
    }
}
