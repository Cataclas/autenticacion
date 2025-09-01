package co.com.crediya.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
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
                        .title("Microservicio de Autenticación")
                        .version("1.0.0")
                        .description("API REST para registro y gestión de usuarios con arquitectura hexagonal")
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
                ));
    }
}