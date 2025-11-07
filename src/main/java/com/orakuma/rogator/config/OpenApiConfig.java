package com.orakuma.rogator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public OpenAPI customOpenAPI() {
        String serverUrl = String.format("/%s", appName);
        Server relativeServer = new Server().url(serverUrl);
        return new OpenAPI().servers(List.of(relativeServer));
    }
}
