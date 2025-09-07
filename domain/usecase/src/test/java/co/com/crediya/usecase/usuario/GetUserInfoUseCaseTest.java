package co.com.crediya.usecase.usuario;

import co.com.crediya.model.common.Logger;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserInfoUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private RolRepository rolRepository;
    
    @Mock
    private Logger logger;
    
    private GetUserInfoUseCase getUserInfoUseCase;
    
    @BeforeEach
    void setUp() {
        getUserInfoUseCase = new GetUserInfoUseCase(usuarioRepository, rolRepository, logger);
    }
    
    @Test
    void deberiaObtenerInformacionUsuarioExitosamente() {
        // Given
        String userId = "user-id";
        
        Usuario usuario = Usuario.builder()
                .idUsuario(userId)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@email.com")
                .idRol("rol-id")
                .active(true)
                .build();
                
        Rol rol = Rol.builder()
                .idRol("rol-id")
                .nombre("CLIENTE")
                .build();
                
        when(usuarioRepository.findById(userId)).thenReturn(Mono.just(usuario));
        when(rolRepository.findById("rol-id")).thenReturn(Mono.just(rol));
        
        // When & Then
        StepVerifier.create(getUserInfoUseCase.getUserInfo(userId))
                .expectNextMatches(result -> 
                    result.getUsuario().getIdUsuario().equals(userId) &&
                    result.getRolNombre().equals("CLIENTE"))
                .verifyComplete();
    }
    
    @Test
    void deberiaFallarCuandoUsuarioNoExiste() {
        // Given
        String userId = "user-inexistente";
        
        when(usuarioRepository.findById(userId)).thenReturn(Mono.empty());
        
        // When & Then
        StepVerifier.create(getUserInfoUseCase.getUserInfo(userId))
                .expectError(RuntimeException.class)
                .verify();
    }
    
    @Test
    void deberiaFallarCuandoRolNoExiste() {
        // Given
        String userId = "user-id";
        
        Usuario usuario = Usuario.builder()
                .idUsuario(userId)
                .idRol("rol-inexistente")
                .build();
                
        when(usuarioRepository.findById(userId)).thenReturn(Mono.just(usuario));
        when(rolRepository.findById("rol-inexistente")).thenReturn(Mono.empty());
        
        // When & Then
        StepVerifier.create(getUserInfoUseCase.getUserInfo(userId))
                .expectError()
                .verify();
    }
    
    @Test
    void deberiaCompletarseConUsuarioYRolValidos() {
        // Given
        String userId = "valid-user";
        
        Usuario usuario = Usuario.builder()
                .idUsuario(userId)
                .nombre("María")
                .apellido("García")
                .email("maria@email.com")
                .idRol("asesor-role")
                .build();
                
        Rol rol = Rol.builder()
                .idRol("asesor-role")
                .nombre("ASESOR")
                .descripcion("Asesor de crédito")
                .build();
                
        when(usuarioRepository.findById(userId)).thenReturn(Mono.just(usuario));
        when(rolRepository.findById("asesor-role")).thenReturn(Mono.just(rol));
        
        // When & Then
        StepVerifier.create(getUserInfoUseCase.getUserInfo(userId))
                .expectNextMatches(result -> {
                    return result.getUsuario().getNombre().equals("María") &&
                           result.getUsuario().getApellido().equals("García") &&
                           result.getRolNombre().equals("ASESOR");
                })
                .verifyComplete();
    }
    
    @Test
    void deberiaRetornarResultadoConTodosLosCampos() {
        // Given
        String userId = "complete-user";
        
        Usuario usuario = Usuario.builder()
                .idUsuario(userId)
                .nombre("Carlos")
                .apellido("López")
                .email("carlos@email.com")
                .documentoIdentidad("12345678")
                .telefono("3001234567")
                .idRol("admin-role")
                .salarioBase(5000000.0)
                .active(true)
                .build();
                
        Rol rol = Rol.builder()
                .idRol("admin-role")
                .nombre("ADMINISTRADOR")
                .descripcion("Administrador del sistema")
                .active(true)
                .build();
                
        when(usuarioRepository.findById(userId)).thenReturn(Mono.just(usuario));
        when(rolRepository.findById("admin-role")).thenReturn(Mono.just(rol));
        
        // When & Then
        StepVerifier.create(getUserInfoUseCase.getUserInfo(userId))
                .expectNextMatches(result -> {
                    Usuario resultUsuario = result.getUsuario();
                    return resultUsuario.getIdUsuario().equals(userId) &&
                           resultUsuario.getEmail().equals("carlos@email.com") &&
                           resultUsuario.getDocumentoIdentidad().equals("12345678") &&
                           resultUsuario.getSalarioBase().equals(5000000.0) &&
                           result.getRolNombre().equals("ADMINISTRADOR");
                })
                .verifyComplete();
    }
}