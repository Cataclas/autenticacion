package co.com.crediya.usecase.usuario;

import co.com.crediya.model.auth.gateways.PasswordEncoder;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.DatosInvalidosException;
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
    
    @Mock
    private RolRepository rolRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;
    
    @BeforeEach
    void setUp() {
        registrarUsuarioUseCase = new RegistrarUsuarioUseCase(usuarioRepository, rolRepository, passwordEncoder);
    }
    
    @Test
    void deberiaRegistrarUsuarioExitosamente() {
        // Given
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@email.com")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .idRol("CLIENTE")
                .salarioBase(2500000.0)
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .direccion("Calle 123")
                .password("password123")
                .build();
                
        Rol rol = Rol.builder()
                .idRol("rol-id")
                .nombre("CLIENTE")
                .active(true)
                .build();
                
        when(usuarioRepository.existePorEmail(anyString())).thenReturn(Mono.just(false));
        when(rolRepository.findByNombre(anyString())).thenReturn(Mono.just(rol));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(usuarioRepository.guardar(any(Usuario.class))).thenReturn(Mono.just(usuario));
        
        // When & Then
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectNextMatches(result -> result.getEmail().equals("juan@email.com"))
                .verifyComplete();
    }
    
    @Test
    void deberiaFallarCuandoUsuarioEsNulo() {
        // When & Then
        StepVerifier.create(registrarUsuarioUseCase.registrar(null))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoEmailYaExiste() {
        // Given
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("existente@email.com")
                .documentoIdentidad("12345678")
                .idRol("CLIENTE")
                .salarioBase(2500000.0)
                .password("password123")
                .build();
                
        when(usuarioRepository.existePorEmail(anyString())).thenReturn(Mono.just(true));
        
        // When & Then
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoRolNoExiste() {
        // Given
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("test@email.com")
                .documentoIdentidad("12345678")
                .idRol("ROL_INEXISTENTE")
                .salarioBase(2500000.0)
                .password("password123")
                .build();
                
        when(usuarioRepository.existePorEmail(anyString())).thenReturn(Mono.just(false));
        when(rolRepository.findByNombre(anyString())).thenReturn(Mono.empty());
        
        // When & Then
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoNombreEsNulo() {
        // Given
        Usuario usuario = Usuario.builder()
                .nombre(null)
                .apellido("Pérez")
                .email("test@email.com")
                .documentoIdentidad("12345678")
                .idRol("CLIENTE")
                .salarioBase(2500000.0)
                .password("password123")
                .build();
        
        // When & Then
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoEmailEsInvalido() {
        // Given
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("email-invalido")
                .documentoIdentidad("12345678")
                .idRol("CLIENTE")
                .salarioBase(2500000.0)
                .password("password123")
                .build();
        
        // When & Then
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoSalarioEsNulo() {
        // Given
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("test@email.com")
                .documentoIdentidad("12345678")
                .idRol("CLIENTE")
                .salarioBase(null)
                .password("password123")
                .build();
        
        // When & Then
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoPasswordEsNulo() {
        // Given
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .email("test@email.com")
                .documentoIdentidad("12345678")
                .idRol("CLIENTE")
                .salarioBase(2500000.0)
                .password(null)
                .build();
        
        // When & Then
        StepVerifier.create(registrarUsuarioUseCase.registrar(usuario))
                .expectError(DatosInvalidosException.class)
                .verify();
    }
}