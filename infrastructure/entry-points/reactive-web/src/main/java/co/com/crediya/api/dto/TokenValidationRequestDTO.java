package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud de validación de token")
public class TokenValidationRequestDTO {
    @Schema(description = "Token JWT a validar", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;
}