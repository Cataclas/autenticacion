package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.auth.ServiceTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceTokenHandler {
    
    private final ServiceTokenGenerator serviceTokenGenerator;
    
    @Value("${jwt.service-secret}")
    private String serviceSecret;
    
    public Mono<ServerResponse> getServiceToken(ServerRequest serverRequest) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        
        return serverRequest.bodyToMono(ServiceTokenRequest.class)
                .flatMap(request -> {
                    // Validar credenciales del servicio
                    if (!isValidServiceCredentials(request)) {
                        log.warn("[{}] Intento de acceso no autorizado para servicio: {}", traceId, request.getServiceName());
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .bodyValue(ErrorResponseDTO.of("Credenciales de servicio inválidas", "INVALID_SERVICE_CREDENTIALS"));
                    }
                    
                    String token = serviceTokenGenerator.generateServiceToken(request.getServiceName());
                    log.info("[{}] Token de servicio generado para: {}", traceId, request.getServiceName());
                    
                    return ServerResponse.ok().bodyValue(Map.of(
                            "service_token", token,
                            "token_type", "service",
                            "expires_in", 86400 // 24 horas en segundos
                    ));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("[{}] Error generando token de servicio: {}", traceId, ex.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponseDTO.of("Error interno del servidor", "ERROR_INTERNO", traceId));
                });
    }
    
    private boolean isValidServiceCredentials(ServiceTokenRequest request) {
        // En producción, esto debería validar contra una base de datos de servicios registrados
        return "solicitudes".equals(request.getServiceName()) && 
               serviceSecret.equals(request.getServiceSecret());
    }
    
    public static class ServiceTokenRequest {
        private String serviceName;
        private String serviceSecret;
        
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        
        public String getServiceSecret() { return serviceSecret; }
        public void setServiceSecret(String serviceSecret) { this.serviceSecret = serviceSecret; }
    }
}