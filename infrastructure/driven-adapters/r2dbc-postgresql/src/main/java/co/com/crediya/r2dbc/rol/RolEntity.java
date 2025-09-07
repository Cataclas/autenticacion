package co.com.crediya.r2dbc.rol;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("rol")
public class RolEntity {
    @Id
    @Column("id_rol")
    private String idRol;
    
    @Column("nombre")
    private String nombre;
    
    @Column("descripcion")
    private String descripcion;
    
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