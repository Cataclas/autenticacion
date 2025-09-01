package co.com.crediya.r2dbc.usuario;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table("usuario")
public class UsuarioEntity {
    @Id
    @Column("id_usuario")
    private String idUsuario;
    
    @Column("nombre")
    private String nombre;
    
    @Column("apellido")
    private String apellido;
    
    @Column("email")
    private String email;
    
    @Column("documento_identidad")
    private String documentoIdentidad;
    
    @Column("telefono")
    private String telefono;
    
    @Column("id_rol")
    private String idRol;
    
    @Column("salario_base")
    private Double salarioBase;
    
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column("direccion")
    private String direccion;
    
    // Campos de auditoría
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;
    
    @Column("created_by")
    private String createdBy;
    
    @Column("updated_by")
    private String updatedBy;
    
    @Column("active")
    private Boolean active;
}