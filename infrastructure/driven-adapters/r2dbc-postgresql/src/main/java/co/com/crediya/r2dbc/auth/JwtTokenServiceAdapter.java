package co.com.crediya.r2dbc.auth;

import co.com.crediya.model.auth.exceptions.TokenGenerationException;
import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenServiceAdapter implements TokenService {
    
    private final SecretKey secretKey;
    private final long expirationHours;
    private final RolRepository rolRepository;
    
    public JwtTokenServiceAdapter(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-hours:24}") long expirationHours,
            RolRepository rolRepository) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationHours = expirationHours;
        this.rolRepository = rolRepository;
    }
    
    @Override
    public Mono<String> generateToken(Usuario usuario) {
        try {
            Instant now = Instant.now();
            Instant expiration = now.plus(expirationHours, ChronoUnit.HOURS);
            
            String token = Jwts.builder()
                    .subject(usuario.getIdUsuario())
                    .claim("role", usuario.getIdRol()) // Usar ID del rol temporalmente
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(expiration))
                    .signWith(secretKey)
                    .compact();
                    
            return Mono.just(token);
        } catch (Exception e) {
            log.error("Error generando token JWT: {}", e.getMessage());
            return Mono.error(new TokenGenerationException("Error generando token", e));
        }
    }
    
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            log.debug("Token válido");
            return true;
        } catch (Exception e) {
            log.error("Token inválido - Error: {} - Token: {}", e.getMessage(), token.substring(0, Math.min(50, token.length())) + "...");
            return false;
        }
    }
    
    @Override
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }
    
    @Override
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
    
    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }
    
    @Override
    public Long getExpirationTime() {
        return expirationHours * 3600; // Retorna en segundos
    }
    
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}