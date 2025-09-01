package co.com.crediya.model.usuario;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deberiaCrearUsuarioConBuilder() {
        // Given
        LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
        LocalDateTime ahora = LocalDateTime.now();

        // When
        Usuario usuario = Usuario.builder()
                .idUsuario("USR001")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@email.com")
                .documentoIdentidad("12345678")
                .salarioBase(2500000.0)
                .fechaNacimiento(fechaNacimiento)
                .createdAt(ahora)
                .updatedAt(ahora)
                .createdBy("SYSTEM")
                .active(true)
                .build();

        // Then
        assertEquals("USR001", usuario.getIdUsuario());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Pérez", usuario.getApellido());
        assertEquals("juan@email.com", usuario.getEmail());
        assertEquals("12345678", usuario.getDocumentoIdentidad());
        assertEquals(2500000.0, usuario.getSalarioBase());
        assertEquals(fechaNacimiento, usuario.getFechaNacimiento());
        assertEquals(ahora, usuario.getCreatedAt());
        assertEquals(ahora, usuario.getUpdatedAt());
        assertTrue(usuario.getActive());
    }

    @Test
    void deberiaCrearUsuarioSinId() {
        // When
        Usuario usuario = Usuario.builder()
                .nombre("Ana")
                .apellido("García")
                .email("ana@email.com")
                .documentoIdentidad("87654321")
                .salarioBase(3000000.0)
                .fechaNacimiento(LocalDate.of(1985, 5, 15))
                .build();

        // Then
        assertNull(usuario.getIdUsuario());
        assertEquals("Ana", usuario.getNombre());
        assertEquals("García", usuario.getApellido());
        assertEquals("ana@email.com", usuario.getEmail());
    }

    @Test
    void deberiaModificarUsuarioConToBuilder() {
        // Given
        Usuario usuarioOriginal = Usuario.builder()
                .nombre("Carlos")
                .apellido("López")
                .email("carlos@email.com")
                .documentoIdentidad("11111111")
                .salarioBase(2000000.0)
                .fechaNacimiento(LocalDate.of(1992, 3, 10))
                .build();

        // When
        Usuario usuarioModificado = usuarioOriginal.toBuilder()
                .salarioBase(2800000.0)
                .build();

        // Then
        assertEquals("Carlos", usuarioModificado.getNombre());
        assertEquals("López", usuarioModificado.getApellido());
        assertEquals(2800000.0, usuarioModificado.getSalarioBase());
        assertEquals(usuarioOriginal.getFechaNacimiento(), usuarioModificado.getFechaNacimiento());
    }

    @Test
    void deberiaSerIgualConMismosValores() {
        // Given
        LocalDate fecha = LocalDate.of(1990, 1, 1);
        Usuario usuario1 = Usuario.builder()
                .idUsuario("USR001")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@email.com")
                .documentoIdentidad("12345678")
                .salarioBase(2500000.0)
                .fechaNacimiento(fecha)
                .build();

        Usuario usuario2 = Usuario.builder()
                .idUsuario("USR001")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@email.com")
                .documentoIdentidad("12345678")
                .salarioBase(2500000.0)
                .fechaNacimiento(fecha)
                .build();

        // Then
        assertEquals(usuario1, usuario2);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void deberiaSerDiferenteConValoresDistintos() {
        // Given
        Usuario usuario1 = Usuario.builder()
                .idUsuario("USR001")
                .nombre("Juan")
                .email("juan@email.com")
                .build();

        Usuario usuario2 = Usuario.builder()
                .idUsuario("USR002")
                .nombre("Ana")
                .email("ana@email.com")
                .build();

        // Then
        assertNotEquals(usuario1, usuario2);
    }
}