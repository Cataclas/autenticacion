package co.com.crediya.usecase.auth;

import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.common.Logger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ValidateTokenUseCase {
    
    private final TokenService tokenService;
    private final Logger logger;
    
    public Mono<TokenValidationResult> validateToken(String token) {
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        logger.info("[{}] Validando token para microservicio externo", traceId);
        
        if (token == null || token.trim().isEmpty()) {
            logger.warn("[{}] Token vacío o nulo", traceId);
            return Mono.just(TokenValidationResult.invalid());
        }
        
        try {
            boolean isValid = tokenService.validateToken(token);
            
            if (!isValid) {
                logger.warn("[{}] Token inválido", traceId);
                return Mono.just(TokenValidationResult.invalid());
            }
            
            String userId = tokenService.extractUserId(token);
            String roleId = tokenService.extractRole(token);
            
            logger.info("[{}] Token válido para usuario: {} con rol: {}", traceId, userId, roleId);
            
            return Mono.just(TokenValidationResult.builder()
                    .valid(true)
                    .userId(userId)
                    .roleId(roleId)
                    .expiresIn(tokenService.getExpirationTime())
                    .build());
                    
        } catch (Exception e) {
            logger.error("[{}] Error validando token: {}", traceId, e.getMessage(), e);
            return Mono.just(TokenValidationResult.invalid());
        }
    }
}