package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.UsuarioRequestDTO;
import co.com.crediya.api.dto.UsuarioResponseDTO;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.DatosInvalidosException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toDomain(UsuarioRequestDTO dto) {
        if (dto == null) {
            throw new DatosInvalidosException("Los datos del usuario son requeridos");
        }
        
        return Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .documentoIdentidad(dto.getDocumentoIdentidad())
                .salarioBase(dto.getSalarioBase())
                .fechaNacimiento(dto.getFechaNacimiento())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .build();
    }

    public UsuarioResponseDTO toResponse(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setDocumentoIdentidad(usuario.getDocumentoIdentidad());
        dto.setSalarioBase(usuario.getSalarioBase());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setDireccion(usuario.getDireccion());
        dto.setTelefono(usuario.getTelefono());
        dto.setCreatedAt(usuario.getCreatedAt());
        return dto;
    }
}