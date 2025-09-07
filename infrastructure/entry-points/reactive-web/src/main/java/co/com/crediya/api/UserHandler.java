package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.UserInfoRequestDTO;
import co.com.crediya.api.dto.UserInfoResponseDTO;
import co.com.crediya.usecase.usuario.GetUserInfoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {
    
    private final GetUserInfoUseCase getUserInfoUseCase;
    
    public Mono<ServerResponse> getUserInfo(ServerRequest serverRequest) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        
        return serverRequest.bodyToMono(UserInfoRequestDTO.class)
                .flatMap(request -> {
                    log.info("[{}] Consultando información de usuario", traceId);
                    
                    return getUserInfoUseCase.getUserInfo(request.getUserId())
                            .map(result -> UserInfoResponseDTO.builder()
                                    .idUsuario(result.getUsuario().getIdUsuario())
                                    .nombreCompleto(result.getUsuario().getNombre() + " " + result.getUsuario().getApellido())
                                    .email(result.getUsuario().getEmail())
                                    .documentoIdentidad(result.getUsuario().getDocumentoIdentidad())
                                    .telefono(result.getUsuario().getTelefono())
                                    .salarioBase(BigDecimal.valueOf(result.getUsuario().getSalarioBase()))
                                    .fechaNacimiento(result.getUsuario().getFechaNacimiento())
                                    .direccion(result.getUsuario().getDireccion())
                                    .rol(result.getRolNombre())
                                    .build())
                            .flatMap(response -> {
                                log.info("[{}] Información de usuario obtenida exitosamente", traceId);
                                return ServerResponse.ok().bodyValue(response);
                            });
                })
                .onErrorResume(RuntimeException.class, ex -> {
                    if (ex.getMessage().contains("no encontrado")) {
                        log.warn("[{}] Usuario no encontrado", traceId);
                        return ServerResponse.status(HttpStatus.NOT_FOUND)
                                .bodyValue(ErrorResponseDTO.of("Usuario no encontrado", "USER_NOT_FOUND"));
                    }
                    log.error("[{}] Error interno consultando usuario: {}", traceId, ex.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of(
                                "Error interno del servidor", 
                                "ERROR_INTERNO", 
                                traceId));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("[{}] Error interno consultando usuario: {}", traceId, ex.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of(
                                "Error interno del servidor", 
                                "ERROR_INTERNO", 
                                traceId));
                });
    }
}