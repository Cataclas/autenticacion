package co.com.crediya.usecase.auth;

import co.com.crediya.model.auth.exceptions.InvalidCredentialsException;
import co.com.crediya.model.auth.gateways.PasswordEncoder;
import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.common.Logger;
import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final Logger logger;
    
    public Mono<LoginResult> authenticate(String email, String password) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        logger.info("[{}] Iniciando autenticación para email: {}", traceId, 
            email != null ? email.substring(0, Math.min(email.length(), 3)) + "***" : "null");
        
        return usuarioRepository.findByEmailAndActive(email, true)
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("[{}] Usuario no encontrado o inactivo", traceId);
                    return Mono.error(new InvalidCredentialsException("Credenciales inválidas"));
                }))
                .flatMap(usuario -> validatePassword(usuario, password, traceId))
                .flatMap(usuario -> generateLoginResult(usuario, traceId))
                .doOnSuccess(result -> logger.info("[{}] Autenticación exitosa para rol: {}", 
                    traceId, result.getUsuario().getIdRol()))
                .doOnError(error -> logger.error("[{}] Error en autenticación: {}", 
                    traceId, error.getMessage()));
    }
    
    private Mono<Usuario> validatePassword(Usuario usuario, String password, String traceId) {
        logger.debug("[{}] Validando password - Hash en BD: {}", traceId, usuario.getPassword());
        logger.debug("[{}] Password recibido: {}", traceId, password);
        
        // Log para ver cómo queda el password encriptado
        String encryptedPassword = passwordEncoder.encode(password);
        logger.debug("[{}] Password del request encriptado con BCrypt: {}", traceId, encryptedPassword);
        
        boolean matches = passwordEncoder.matches(password, usuario.getPassword());
        logger.debug("[{}] Password matches: {}", traceId, matches);
        
        if (!matches) {
            logger.warn("[{}] Contraseña incorrecta", traceId);
            return Mono.error(new InvalidCredentialsException("Credenciales inválidas"));
        }
        return Mono.just(usuario);
    }
    
    private Mono<LoginResult> generateLoginResult(Usuario usuario, String traceId) {
        return tokenService.generateToken(usuario)
                .map(token -> {
                    logger.debug("[{}] Token generado exitosamente", traceId);
                    return LoginResult.builder()
                            .token(token)
                            .tokenType("Bearer")
                            .expiresIn(tokenService.getExpirationTime())
                            .usuario(usuario)
                            .build();
                })
                .onErrorMap(e -> {
                    logger.error("[{}] Error generando token: {}", traceId, e.getMessage());
                    return new InvalidCredentialsException("Error en la autenticación");
                });
    }
}