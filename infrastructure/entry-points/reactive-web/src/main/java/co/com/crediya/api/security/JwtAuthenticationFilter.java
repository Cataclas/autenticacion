package co.com.crediya.api.security;

import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.api.auth.ServiceTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    
    private final TokenService tokenService;
    private final ServiceTokenGenerator serviceTokenGenerator;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // Rutas públicas
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }
        
        // Endpoints de usuario - validación especial
        if (isUserEndpoint(path)) {
            if (!canAccessUserEndpoint(exchange)) {
                log.warn("Acceso denegado a endpoint de usuario desde IP: {}", getClientIp(exchange));
                return unauthorized(exchange);
            }
            return chain.filter(exchange);
        }
        
        String token = extractToken(exchange);
        if (token == null) {
            return unauthorized(exchange);
        }
        
        // VALIDACIÓN CRÍTICA: Verificar firma JWT
        if (!tokenService.validateToken(token)) {
            log.warn("Token inválido o modificado detectado desde IP: {}", 
                getClientIp(exchange));
            return unauthorized(exchange);
        }
        
        // Agregar contexto de usuario para otros servicios
        String userId = tokenService.extractUserId(token);
        String role = tokenService.extractRole(token);
        
        exchange.getAttributes().put("userId", userId);
        exchange.getAttributes().put("userRole", role);
        
        return chain.filter(exchange);
    }
    
    private boolean isPublicPath(String path) {
        return path.equals("/api/v1/login") ||
               path.equals("/api/v1/validate-token") ||
               path.equals("/api/v1/service-token") ||
               path.startsWith("/swagger") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/webjars/swagger-ui") ||
               path.startsWith("/actuator");
    }
    
    private boolean isUserEndpoint(String path) {
        return path.equals("/api/v1/users/info");
    }
    
    private boolean canAccessUserEndpoint(ServerWebExchange exchange) {
        String token = extractToken(exchange);
        if (token == null) {
            return false;
        }
        
        // 1. Verificar si es token de servicio
        if (serviceTokenGenerator.validateServiceToken(token)) {
            String serviceName = serviceTokenGenerator.extractServiceName(token);
            log.debug("Acceso de servicio autorizado: {}", serviceName);
            return true;
        }
        
        // 2. Verificar si es usuario ADMIN autenticado
        try {
            if (tokenService.validateToken(token)) {
                String role = tokenService.extractRole(token);
                boolean isAdmin = "550e8400-e29b-41d4-a716-446655440001".equals(role);
                if (isAdmin) {
                    log.debug("Acceso de administrador autorizado");
                    return true;
                }
            }
        } catch (Exception e) {
            log.debug("Token no es de usuario regular: {}", e.getMessage());
        }
        
        return false;
    }
    
    private String extractToken(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
    
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    
    private String getClientIp(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
    }
}