package co.com.crediya.usecase.usuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private RegistrarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarUsuarioUseCase(usuarioRepository);
    }

    @Test
    void deberiaRegistrarUsuarioExitosamente() {
        // Given
        Usuario usuario = crearUsuarioValido();
        when(usuarioRepository.existePorEmail(anyString())).thenReturn(Mono.just(false));
        when(usuarioRepository.guardar(any(Usuario.class))).thenReturn(Mono.just(usuario));

        // When & Then
        StepVerifier.create(useCase.registrar(usuario))
                .expectNextCount(1)
                .verifyComplete();
    }







    private Usuario crearUsuarioValido() {
        return Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@email.com")
                .documentoIdentidad("12345678")
                .salarioBase(2500000.0)
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();
    }
}