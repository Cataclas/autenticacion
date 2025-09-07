package co.com.crediya.api.dto;

import co.com.crediya.api.dto.validation.ValidRol;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@Schema(description = "Datos para registrar un nuevo usuario")
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @JsonProperty("nombres")
    @Schema(description = "Nombres del usuario", example = "Juan Carlos", required = true)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @JsonProperty("apellidos")
    @Schema(description = "Apellidos del usuario", example = "Pérez García", required = true)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @JsonProperty("correo_electronico")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com", required = true)
    private String email;

    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0.0", message = "El salario debe ser mayor o igual a 0")
    @DecimalMax(value = "15000000.0", message = "El salario debe ser menor o igual a 15.000.000")
    @JsonProperty("salario_base")
    @Schema(description = "Salario base del usuario", example = "2500000.0", required = true, minimum = "0", maximum = "15000000")
    private Double salarioBase;
    
    @NotBlank(message = "El documento de identidad es obligatorio")
    @JsonProperty("documento_identidad")
    @Schema(description = "Documento de identidad del usuario", example = "12345678", required = true)
    private String documentoIdentidad;

    @JsonProperty("fecha_nacimiento")
    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-01-15")
    private LocalDate fechaNacimiento;

    @JsonProperty("direccion")
    @Schema(description = "Dirección de residencia del usuario", example = "Calle 123 #45-67")
    private String direccion;

    @JsonProperty("telefono")
    @Schema(description = "Número de teléfono del usuario", example = "3001234567")
    private String telefono;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @JsonProperty("password")
    @Schema(description = "Contraseña del usuario", example = "miPassword123", required = true, minLength = 6)
    private String password;
    
    @NotBlank(message = "El rol es obligatorio")
    @ValidRol
    @JsonProperty("rol")
    @Schema(description = "Rol del usuario", example = "SOLICITANTE", required = true, allowableValues = {"ADMINISTRADOR", "ASESOR", "SOLICITANTE"})
    private String rol;
}