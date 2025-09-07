package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Respuesta de validación de token")
public class TokenValidationResponseDTO {
    @Schema(description = "Indica si el token es válido", example = "true")
    private boolean valid;
    
    @Schema(description = "ID del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
    private String userId;
    
    @Schema(description = "ID del rol del usuario", example = "550e8400-e29b-41d4-a716-446655440001")
    private String roleId;
    
    @Schema(description = "Tiempo restante de expiración en segundos", example = "3600")
    private Long expiresIn;
}