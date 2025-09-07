package co.com.crediya.usecase.auth;

import co.com.crediya.model.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResultTest {

    @Test
    void deberiaCrearLoginResultConBuilder() {
        // Given
        Usuario usuario = Usuario.builder()
                .idUsuario("123")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@test.com")
                .build();
        
        // When
        LoginResult result = LoginResult.builder()
                .token("jwt-token-123")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .usuario(usuario)
                .build();
        
        // Then
        assertEquals("jwt-token-123", result.getToken());
        assertEquals("Bearer", result.getTokenType());
        assertEquals(3600L, result.getExpiresIn());
        assertEquals(usuario, result.getUsuario());
    }

    @Test
    void deberiaPermitirValoresNulos() {
        // When
        LoginResult result = LoginResult.builder().build();
        
        // Then
        assertNull(result.getToken());
        assertNull(result.getTokenType());
        assertNull(result.getExpiresIn());
        assertNull(result.getUsuario());
    }
}