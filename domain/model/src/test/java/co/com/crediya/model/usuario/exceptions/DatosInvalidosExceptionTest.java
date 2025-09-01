package co.com.crediya.model.usuario.exceptions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatosInvalidosExceptionTest {

    @Test
    void deberiaCrearExcepcionConMensajeUnico() {
        // Given
        String mensaje = "Email inválido";

        // When
        DatosInvalidosException exception = new DatosInvalidosException(mensaje);

        // Then
        assertEquals(mensaje, exception.getMessage());
        assertTrue(exception.getErrores().contains(mensaje));
        assertEquals(1, exception.getErrores().size());
    }

    @Test
    void deberiaCrearExcepcionConMultiplesErrores() {
        // Given
        List<String> errores = Arrays.asList(
            "Nombre es requerido",
            "Email inválido",
            "Salario debe ser positivo"
        );

        // When
        DatosInvalidosException exception = new DatosInvalidosException(errores);

        // Then
        assertEquals("Nombre es requerido, Email inválido, Salario debe ser positivo", 
                     exception.getMessage());
        assertEquals(errores, exception.getErrores());
        assertEquals(3, exception.getErrores().size());
    }

    @Test
    void deberiaRetornarListaInmutableDeErrores() {
        // Given
        List<String> errores = Arrays.asList("Error 1", "Error 2");
        DatosInvalidosException exception = new DatosInvalidosException(errores);

        // When & Then
        assertThrows(UnsupportedOperationException.class, () -> {
            exception.getErrores().add("Error 3");
        });
    }
}