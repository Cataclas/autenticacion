package co.com.crediya.model.usuario.exceptions;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatosInvalidosExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensaje() {
        // Given
        String mensaje = "Datos inválidos";
        
        // When
        DatosInvalidosException exception = new DatosInvalidosException(mensaje);
        
        // Then
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void deberiaCrearExcepcionConListaDeErrores() {
        // Given
        List<String> errores = Arrays.asList("Error 1", "Error 2", "Error 3");
        
        // When
        DatosInvalidosException exception = new DatosInvalidosException(errores);
        
        // Then
        assertTrue(exception.getMessage().contains("Error 1"));
        assertTrue(exception.getMessage().contains("Error 2"));
        assertTrue(exception.getMessage().contains("Error 3"));
    }

    @Test
    void deberiaCrearExcepcionConListaVacia() {
        // Given
        List<String> errores = Arrays.asList();
        
        // When
        DatosInvalidosException exception = new DatosInvalidosException(errores);
        
        // Then
        assertNotNull(exception.getMessage());
    }
}