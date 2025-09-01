package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {
    
    @JsonProperty("id_usuario")
    private String idUsuario;
    
    @JsonProperty("nombres")
    private String nombre;
    
    @JsonProperty("apellidos")
    private String apellido;
    
    @JsonProperty("correo_electronico")
    private String email;
    
    @JsonProperty("documento_identidad")
    private String documentoIdentidad;
    
    @JsonProperty("salario_base")
    private Double salarioBase;
    
    @JsonProperty("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @JsonProperty("direccion")
    private String direccion;
    
    @JsonProperty("telefono")
    private String telefono;
    
    @JsonProperty("fecha_creacion")
    private LocalDateTime createdAt;
}