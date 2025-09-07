package co.com.crediya.api;

import co.com.crediya.api.config.RoleConfig;
import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.LoginRequestDTO;
import co.com.crediya.api.dto.LoginResponseDTO;
import co.com.crediya.api.dto.TokenValidationRequestDTO;
import co.com.crediya.api.dto.TokenValidationResponseDTO;
import co.com.crediya.model.auth.exceptions.InvalidCredentialsException;
import co.com.crediya.model.common.UserContext;
import co.com.crediya.usecase.auth.LoginUseCase;
import co.com.crediya.usecase.auth.ValidateTokenUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {
    
    private final LoginUseCase loginUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;
    private final RoleConfig roleConfig;
    
    public Mono<ServerResponse> iniciarSesion(ServerRequest serverRequest) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        log.info("[{}] Iniciando proceso de autenticación", traceId);
        
        return serverRequest.bodyToMono(LoginRequestDTO.class)
                .doOnNext(dto -> log.debug("[{}] Procesando login para email: {}", traceId, 
                    dto.getEmail() != null ? dto.getEmail().substring(0, 3) + "***" : "null"))
                .flatMap(loginRequest -> loginUseCase.authenticate(loginRequest.getEmail(), loginRequest.getPassword())
                        .contextWrite(UserContext.withUser("SYSTEM", loginRequest.getEmail())))
                .map(resultado -> construirRespuestaLogin(resultado, traceId))
                .flatMap(response -> {
                    log.info("[{}] Autenticación exitosa", traceId);
                    return ServerResponse.ok().bodyValue(response);
                })
                .onErrorResume(InvalidCredentialsException.class, ex -> {
                    log.warn("[{}] Credenciales inválidas", traceId);
                    return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                            .bodyValue(ErrorResponseDTO.of(ex.getMessage(), "CREDENCIALES_INVALIDAS"));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("[{}] Error interno en autenticación: {}", traceId, ex.getClass().getSimpleName(), ex);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of(
                                "Error interno del servidor. Nuestro equipo técnico ha sido notificado", 
                                "ERROR_INTERNO", 
                                traceId));
                });
    }
    
    public Mono<ServerResponse> validarToken(ServerRequest serverRequest) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        log.info("[{}] Iniciando validación de token", traceId);
        
        return serverRequest.bodyToMono(TokenValidationRequestDTO.class)
                .flatMap(request -> validateTokenUseCase.validateToken(request.getToken()))
                .map(result -> TokenValidationResponseDTO.builder()
                        .valid(result.isValid())
                        .userId(result.getUserId())
                        .roleId(result.getRoleId())
                        .expiresIn(result.getExpiresIn())
                        .build())
                .flatMap(response -> {
                    if (response.isValid()) {
                        log.info("[{}] Token válido", traceId);
                        return ServerResponse.ok().bodyValue(response);
                    } else {
                        log.warn("[{}] Token inválido", traceId);
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .bodyValue(TokenValidationResponseDTO.builder()
                                        .valid(false)
                                        .build());
                    }
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("[{}] Error validando token: {}", traceId, ex.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of(
                                "Error interno del servidor", 
                                "ERROR_INTERNO", 
                                traceId));
                });
    }
    
    private String mapRoleToFrontend(String roleId) {
        // Mapear IDs a nombres para frontend
        if (roleId.equals("550e8400-e29b-41d4-a716-446655440001")) return "ADMIN";
        if (roleId.equals("550e8400-e29b-41d4-a716-446655440003")) return "ASESOR";
        return "CLIENTE";
    }
    
    private LoginResponseDTO construirRespuestaLogin(co.com.crediya.usecase.auth.LoginResult resultado, String traceId) {
        log.debug("[{}] Construyendo respuesta de login", traceId);
        
        String displayName = resultado.getUsuario().getNombre() + " " + 
                resultado.getUsuario().getApellido().substring(0, 1) + ".";
        
        return LoginResponseDTO.builder()
                .token(resultado.getToken())
                .user(LoginResponseDTO.UserInfoDTO.builder()
                        .displayName(displayName)
                        .role(mapRoleToFrontend(resultado.getUsuario().getIdRol()))
                        .build())
                .build();
    }
}