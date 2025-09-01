package co.com.crediya.model.usuario.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioYaExisteExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensaje() {
        // Given
        String mensaje = "Usuario ya existe";

        // When
        UsuarioYaExisteException exception = new UsuarioYaExisteException(mensaje);

        // Then
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void deberiaSerRuntimeException() {
        // Given
        String email = "test@email.com";

        // When
        UsuarioYaExisteException exception = new UsuarioYaExisteException(email);

        // Then
        assertTrue(exception instanceof RuntimeException);
    }
}