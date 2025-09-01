package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.UsuarioRequestDTO;
import co.com.crediya.api.mapper.UsuarioMapper;
import co.com.crediya.model.usuario.exceptions.DatosInvalidosException;
import co.com.crediya.model.usuario.exceptions.UsuarioYaExisteException;
import co.com.crediya.usecase.usuario.RegistrarUsuarioUseCase;

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
public class Handler {
    
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final UsuarioMapper usuarioMapper;


    public Mono<ServerResponse> registrarUsuario(ServerRequest serverRequest) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        log.info("[{}] Iniciando registro de usuario", traceId);
        
        return serverRequest.bodyToMono(UsuarioRequestDTO.class)
                .doOnNext(dto -> log.debug("[{}] Procesando solicitud para documento: {}", traceId, 
                    dto.getDocumentoIdentidad() != null ? dto.getDocumentoIdentidad().substring(0, 3) + "***" : "null"))
                .map(usuarioMapper::toDomain)
                .flatMap(registrarUsuarioUseCase::registrar)
                .map(usuarioMapper::toResponse)
                .flatMap(response -> {
                    log.info("[{}] Usuario registrado exitosamente - ID: {}", traceId, response.getIdUsuario());
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(response);
                })
                .onErrorResume(DatosInvalidosException.class, ex -> {
                    log.warn("[{}] Errores de validación: {}", traceId, ex.getErrores().size());
                    log.debug("[{}] Detalles de validación: {}", traceId, ex.getErrores());
                    return ServerResponse.badRequest()
                            .bodyValue(ErrorResponseDTO.of(ex.getErrores(), "INVALID_DATA"));
                })
                .onErrorResume(UsuarioYaExisteException.class, ex -> {
                    log.warn("[{}] Intento de registro duplicado", traceId);
                    return ServerResponse.status(HttpStatus.CONFLICT)
                            .bodyValue(ErrorResponseDTO.of(ex.getMessage(), "USER_ALREADY_EXISTS"));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("[{}] Error interno en registro de usuario: {}", traceId, ex.getClass().getSimpleName(), ex);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of("Error interno del servidor", "INTERNAL_ERROR"));
                });
    }
}