package co.com.crediya.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "roles")
public class RoleConfig {
    private String admin;
    private String asesor;
    private String solicitante;
}