package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "Request para obtener información de usuario")
public class UserInfoRequestDTO {
    
    @NotBlank(message = "El ID de usuario es requerido")
    @Schema(description = "ID del usuario a consultar", example = "550e8400-e29b-41d4-a716-446655440005", required = true)
    private String userId;
}