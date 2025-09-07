package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Información básica del usuario")
public class UserInfoResponseDTO {
    
    @Schema(description = "ID del usuario", example = "550e8400-e29b-41d4-a716-446655440005")
    private String idUsuario;
    
    @Schema(description = "Nombre completo", example = "María Cliente")
    private String nombreCompleto;
    
    @Schema(description = "Email", example = "cliente@crediya.com")
    private String email;
    
    @Schema(description = "Documento de identidad", example = "87654321")
    private String documentoIdentidad;
    
    @Schema(description = "Teléfono", example = "3009876543")
    private String telefono;
    
    @Schema(description = "Salario base", example = "2500000.00")
    private BigDecimal salarioBase;
    
    @Schema(description = "Fecha de nacimiento", example = "1992-08-20")
    private LocalDate fechaNacimiento;
    
    @Schema(description = "Dirección", example = "Carrera 456 #78-90")
    private String direccion;
    
    @Schema(description = "Rol del usuario", example = "CLIENTE")
    private String rol;
}