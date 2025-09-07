package co.com.crediya.api.auth;

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
public class ServiceTokenGenerator {
    
    private final SecretKey serviceSecretKey;
    private final long serviceExpirationHours;
    
    public ServiceTokenGenerator(
            @Value("${jwt.service-secret}") String serviceSecret,
            @Value("${jwt.service-expiration-hours:24}") long serviceExpirationHours) {
        this.serviceSecretKey = Keys.hmacShaKeyFor(serviceSecret.getBytes());
        this.serviceExpirationHours = serviceExpirationHours;
    }
    
    public String generateServiceToken(String serviceName) {
        Instant now = Instant.now();
        Instant expiration = now.plus(serviceExpirationHours, ChronoUnit.HOURS);
        
        return Jwts.builder()
                .subject("service")
                .claim("service_name", serviceName)
                .claim("type", "service_token")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(serviceSecretKey)
                .compact();
    }
    
    public boolean validateServiceToken(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(serviceSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
                    
            return "service".equals(claims.getSubject()) && 
                   "service_token".equals(claims.get("type"));
        } catch (Exception e) {
            log.debug("Token de servicio inválido: {}", e.getMessage());
            return false;
        }
    }
    
    public String extractServiceName(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(serviceSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("service_name", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}