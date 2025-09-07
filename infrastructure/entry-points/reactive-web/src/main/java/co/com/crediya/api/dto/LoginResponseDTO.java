package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Respuesta de autenticación exitosa")
public class LoginResponseDTO {
    @Schema(description = "Token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Información mínima para UI")
    private UserInfoDTO user;
    
    @Data
    @Builder
    @Schema(description = "Información mínima para UI")
    public static class UserInfoDTO {
        @Schema(description = "Nombre completo", example = "Juan Carlos P.")
        private String displayName;
        
        @Schema(description = "Rol para permisos UI", example = "SOLICITANTE")
        private String role;
    }
}