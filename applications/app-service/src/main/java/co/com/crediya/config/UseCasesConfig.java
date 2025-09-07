package co.com.crediya.config;

import co.com.crediya.model.auth.gateways.PasswordEncoder;
import co.com.crediya.model.auth.gateways.TokenService;
import co.com.crediya.model.common.Logger;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.crediya.usecase.auth.LoginUseCase;
import co.com.crediya.usecase.auth.ValidateTokenUseCase;
import co.com.crediya.usecase.usuario.GetUserInfoUseCase;
import co.com.crediya.usecase.usuario.RegistrarUsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {
    
    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        return new RegistrarUsuarioUseCase(usuarioRepository, rolRepository, passwordEncoder);
    }
    
    @Bean
    public LoginUseCase loginUseCase(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            Logger logger) {
        return new LoginUseCase(usuarioRepository, passwordEncoder, tokenService, logger);
    }
    
    @Bean
    public ValidateTokenUseCase validateTokenUseCase(
            TokenService tokenService,
            Logger logger) {
        return new ValidateTokenUseCase(tokenService, logger);
    }
    
    @Bean
    public GetUserInfoUseCase getUserInfoUseCase(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            Logger logger) {
        return new GetUserInfoUseCase(usuarioRepository, rolRepository, logger);
    }
}
