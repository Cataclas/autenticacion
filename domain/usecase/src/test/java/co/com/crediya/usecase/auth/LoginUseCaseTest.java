package co.com.crediya.usecase.auth;

import co.com.crediya.model.auth.exceptions.InvalidCredentialsException;
import co.com.crediya.model.auth.gateways.PasswordEncoder;
import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.common.Logger;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private TokenService tokenService;
    
    @Mock
    private Logger logger;
    
    private LoginUseCase loginUseCase;
    
    @BeforeEach
    void setUp() {
        loginUseCase = new LoginUseCase(usuarioRepository, passwordEncoder, tokenService, logger);
    }
    
    @Test
    void deberiaAutenticarUsuarioExitosamente() {
        // Given
        String email = "test@email.com";
        String password = "password123";
        
        Usuario usuario = Usuario.builder()
                .idUsuario("user-id")
                .email(email)
                .password("$2a$10$hashedPassword")
                .idRol("role-id")
                .active(true)
                .build();
                
        when(usuarioRepository.findByEmailAndActive(anyString(), anyBoolean())).thenReturn(Mono.just(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$newHash");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateToken(any(Usuario.class))).thenReturn(Mono.just("jwt-token"));
        when(tokenService.getExpirationTime()).thenReturn(3600L);
        
        // When & Then
        StepVerifier.create(loginUseCase.authenticate(email, password))
                .expectNextMatches(result -> 
                    result.getToken().equals("jwt-token") &&
                    result.getUsuario().getIdUsuario().equals("user-id") &&
                    result.getTokenType().equals("Bearer"))
                .verifyComplete();
    }
    
    @Test
    void deberiaFallarCuandoUsuarioNoExiste() {
        // Given
        String email = "noexiste@email.com";
        String password = "password123";
        
        when(usuarioRepository.findByEmailAndActive(anyString(), anyBoolean())).thenReturn(Mono.empty());
        
        // When & Then
        StepVerifier.create(loginUseCase.authenticate(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoPasswordEsIncorrecto() {
        // Given
        String email = "test@email.com";
        String password = "passwordIncorrecto";
        
        Usuario usuario = Usuario.builder()
                .idUsuario("user-id")
                .email(email)
                .password("$2a$10$hashedPassword")
                .idRol("role-id")
                .active(true)
                .build();
                
        when(usuarioRepository.findByEmailAndActive(anyString(), anyBoolean())).thenReturn(Mono.just(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$newHash");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        
        // When & Then
        StepVerifier.create(loginUseCase.authenticate(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoTokenGenerationFalla() {
        // Given
        String email = "test@email.com";
        String password = "password123";
        
        Usuario usuario = Usuario.builder()
                .idUsuario("user-id")
                .email(email)
                .password("$2a$10$hashedPassword")
                .idRol("role-id")
                .active(true)
                .build();
                
        when(usuarioRepository.findByEmailAndActive(anyString(), anyBoolean())).thenReturn(Mono.just(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$newHash");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateToken(any(Usuario.class))).thenReturn(Mono.error(new RuntimeException("Token error")));
        
        // When & Then
        StepVerifier.create(loginUseCase.authenticate(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }
    
    @Test
    void deberiaCompletarseConTodosLosCamposDelResult() {
        // Given
        String email = "admin@email.com";
        String password = "admin123";
        
        Usuario usuario = Usuario.builder()
                .idUsuario("admin-id")
                .email(email)
                .nombre("Admin")
                .apellido("Sistema")
                .password("$2a$10$hashedPassword")
                .idRol("admin-role")
                .active(true)
                .build();
                
        when(usuarioRepository.findByEmailAndActive(anyString(), anyBoolean())).thenReturn(Mono.just(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$newHash");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateToken(any(Usuario.class))).thenReturn(Mono.just("admin-jwt-token"));
        when(tokenService.getExpirationTime()).thenReturn(7200L);
        
        // When & Then
        StepVerifier.create(loginUseCase.authenticate(email, password))
                .expectNextMatches(result -> {
                    return result.getToken().equals("admin-jwt-token") &&
                           result.getTokenType().equals("Bearer") &&
                           result.getExpiresIn().equals(7200L) &&
                           result.getUsuario().getNombre().equals("Admin") &&
                           result.getUsuario().getApellido().equals("Sistema");
                })
                .verifyComplete();
    }
}