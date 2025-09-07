package co.com.crediya.usecase.usuario;

import co.com.crediya.model.common.Logger;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetUserInfoUseCase {
    
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final Logger logger;
    
    public Mono<UserInfoResult> getUserInfo(String userId) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        logger.info("[{}] Consultando información de usuario: {}", traceId, userId);
        
        return usuarioRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(usuario -> rolRepository.findById(usuario.getIdRol())
                        .map(rol -> UserInfoResult.builder()
                                .usuario(usuario)
                                .rolNombre(rol.getNombre())
                                .build()))
                .doOnSuccess(result -> logger.info("[{}] Información de usuario obtenida exitosamente", traceId));
    }
    
    @lombok.Builder
    @lombok.Data
    public static class UserInfoResult {
        private Usuario usuario;
        private String rolNombre;
    }
}