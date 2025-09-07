package co.com.crediya.model.auth.gateways;

import co.com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface TokenService {
    Mono<String> generateToken(Usuario usuario);
    boolean validateToken(String token);
    String extractEmail(String token);
    String extractRole(String token);
    String extractUserId(String token);
    Long getExpirationTime();
}