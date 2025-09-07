package co.com.crediya.model.auth.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCredentialsExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensaje() {
        // Given
        String mensaje = "Credenciales inválidas";
        
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(mensaje);
        
        // Then
        assertEquals(mensaje, exception.getMessage());
    }


}