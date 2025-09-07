package co.com.crediya.model.usuario;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deberiaCrearUsuarioConBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
        
        // When
        Usuario usuario = Usuario.builder()
                .idUsuario("123")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@test.com")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .idRol("rol-123")
                .salarioBase(2000000.0)
                .fechaNacimiento(fechaNacimiento)
                .direccion("Calle 123")
                .password("password123")
                .createdAt(now)
                .updatedAt(now)
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .active(true)
                .build();
        
        // Then
        assertEquals("123", usuario.getIdUsuario());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Pérez", usuario.getApellido());
        assertEquals("juan@test.com", usuario.getEmail());
        assertEquals("12345678", usuario.getDocumentoIdentidad());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("rol-123", usuario.getIdRol());
        assertEquals(2000000.0, usuario.getSalarioBase());
        assertEquals(fechaNacimiento, usuario.getFechaNacimiento());
        assertEquals("Calle 123", usuario.getDireccion());
        assertEquals("password123", usuario.getPassword());
        assertEquals(now, usuario.getCreatedAt());
        assertEquals(now, usuario.getUpdatedAt());
        assertEquals("SYSTEM", usuario.getCreatedBy());
        assertEquals("SYSTEM", usuario.getUpdatedBy());
        assertTrue(usuario.getActive());
    }

    @Test
    void deberiaModificarUsuarioConToBuilder() {
        // Given
        Usuario usuarioOriginal = Usuario.builder()
                .idUsuario("123")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@test.com")
                .build();
        
        // When
        Usuario usuarioModificado = usuarioOriginal.toBuilder()
                .nombre("Carlos")
                .email("carlos@test.com")
                .build();
        
        // Then
        assertEquals("123", usuarioModificado.getIdUsuario());
        assertEquals("Carlos", usuarioModificado.getNombre());
        assertEquals("Pérez", usuarioModificado.getApellido());
        assertEquals("carlos@test.com", usuarioModificado.getEmail());
    }
}