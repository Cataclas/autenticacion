package co.com.crediya.r2dbc.usuario;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UsuarioR2dbcRepository extends ReactiveCrudRepository<UsuarioEntity, String> {
    
    Mono<Boolean> existsByEmail(String email);
    
    @Query("INSERT INTO usuario (id_usuario, nombre, apellido, email, documento_identidad, telefono, id_rol, salario_base, fecha_nacimiento, direccion, created_at, updated_at, created_by, updated_by, active) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)")
    Mono<Void> insertUsuario(String idUsuario, String nombre, String apellido, String email, String documentoIdentidad, String telefono, String idRol, Double salarioBase, LocalDate fechaNacimiento, String direccion, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Boolean active);
}