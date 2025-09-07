package co.com.crediya.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Autenticación - Crediya")
                        .version("1.0.0")
                        .description("API para gestión de usuarios y autenticación con JWT")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("desarrollo@crediya.com")))
                .servers(List.of(
                    new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Servidor de desarrollo"),
                    new Server()
                        .url("https://api.crediya.com")
                        .description("Servidor de producción")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtenido del endpoint /api/v1/login")));
    }
}