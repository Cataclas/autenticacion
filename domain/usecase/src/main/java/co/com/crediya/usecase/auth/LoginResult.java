package co.com.crediya.usecase.auth;

import co.com.crediya.model.usuario.Usuario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResult {
    private String token;
    private String tokenType;
    private Long expiresIn;
    private Usuario usuario;
}