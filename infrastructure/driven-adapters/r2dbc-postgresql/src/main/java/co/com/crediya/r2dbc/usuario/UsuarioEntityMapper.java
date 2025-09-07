package co.com.crediya.r2dbc.usuario;

import co.com.crediya.model.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEntityMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(usuario.getIdUsuario());
        entity.setNombre(usuario.getNombre());
        entity.setApellido(usuario.getApellido());
        entity.setEmail(usuario.getEmail());
        entity.setDocumentoIdentidad(usuario.getDocumentoIdentidad());
        entity.setTelefono(usuario.getTelefono());
        entity.setIdRol(usuario.getIdRol());
        entity.setSalarioBase(usuario.getSalarioBase());
        entity.setFechaNacimiento(usuario.getFechaNacimiento());
        entity.setDireccion(usuario.getDireccion());
        entity.setPassword(usuario.getPassword());
        // Campos de auditoría
        entity.setCreatedAt(usuario.getCreatedAt());
        entity.setUpdatedAt(usuario.getUpdatedAt());
        entity.setCreatedBy(usuario.getCreatedBy());
        entity.setUpdatedBy(usuario.getUpdatedBy());
        entity.setActive(usuario.getActive());
        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        return Usuario.builder()
                .idUsuario(entity.getIdUsuario())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .email(entity.getEmail())
                .documentoIdentidad(entity.getDocumentoIdentidad())
                .telefono(entity.getTelefono())
                .idRol(entity.getIdRol())
                .salarioBase(entity.getSalarioBase())
                .fechaNacimiento(entity.getFechaNacimiento())
                .direccion(entity.getDireccion())
                .password(entity.getPassword())
                // Campos de auditoría
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .active(entity.getActive())
                .build();
    }
}