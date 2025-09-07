package co.com.crediya.api.security;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SecurityUtils {
    
    public static Mono<String> getCurrentUserId(ServerWebExchange exchange) {
        String userId = exchange.getAttribute("userId");
        return userId != null ? Mono.just(userId) : Mono.empty();
    }
    
    public static Mono<String> getCurrentUserRole(ServerWebExchange exchange) {
        String role = exchange.getAttribute("userRole");
        return role != null ? Mono.just(role) : Mono.empty();
    }
    
    public static Mono<Boolean> hasRole(ServerWebExchange exchange, String requiredRole) {
        return getCurrentUserRole(exchange)
                .map(role -> role.equals(requiredRole))
                .defaultIfEmpty(false);
    }
    
    public static Mono<Boolean> canRegisterUsers(ServerWebExchange exchange, String adminRole, String asesorRole) {
        return getCurrentUserRole(exchange)
                .map(role -> role.equals(adminRole) || role.equals(asesorRole))
                .defaultIfEmpty(false);
    }
}