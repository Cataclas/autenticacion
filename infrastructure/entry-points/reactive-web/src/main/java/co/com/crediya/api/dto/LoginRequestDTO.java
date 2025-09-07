package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Credenciales para iniciar sesión")
public class LoginRequestDTO {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Schema(description = "Email del usuario", example = "juan.perez@email.com", required = true)
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "miPassword123", required = true)
    private String password;
}