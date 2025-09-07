package co.com.crediya.usecase.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenValidationResult {
    private boolean valid;
    private String userId;
    private String roleId;
    private Long expiresIn;
    
    public static TokenValidationResult invalid() {
        return TokenValidationResult.builder()
                .valid(false)
                .build();
    }
}