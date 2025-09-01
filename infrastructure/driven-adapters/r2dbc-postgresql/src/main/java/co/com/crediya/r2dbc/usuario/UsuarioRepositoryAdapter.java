package co.com.crediya.r2dbc.usuario;

import co.com.crediya.model.usuario.Usuario;
import co.com.crediya.model.usuario.exceptions.UsuarioYaExisteException;
import co.com.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioR2dbcRepository r2dbcRepository;
    private final UsuarioEntityMapper mapper;

    @Override
    @Transactional
    public Mono<Usuario> guardar(Usuario usuario) {
        log.debug("Guardando usuario en BD - documento: {}", 
            usuario.getDocumentoIdentidad().substring(0, 3) + "***");
        
        return r2dbcRepository.insertUsuario(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getDocumentoIdentidad(),
                usuario.getTelefono(),
                usuario.getIdRol(),
                usuario.getSalarioBase(),
                usuario.getFechaNacimiento(),
                usuario.getDireccion(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt(),
                usuario.getCreatedBy(),
                usuario.getUpdatedBy(),
                usuario.getActive()
        ).then(Mono.just(usuario))
        .onErrorMap(ex -> ex.getMessage() != null && ex.getMessage().contains("duplicate key"), ex -> {
            log.warn("Intento de inserción duplicada: {}", ex.getMessage().contains("email") ? "email" : "documento");
            if (ex.getMessage().contains("usuario_email_key")) {
                return new UsuarioYaExisteException("El email ya está registrado");
            } else {
                return new UsuarioYaExisteException("El documento de identidad ya está registrado");
            }
        })
        .doOnSuccess(usuarioGuardado -> log.debug("Usuario persistido exitosamente - ID: {}", usuarioGuardado.getIdUsuario()));
    }

    @Override
    public Mono<Boolean> existePorEmail(String email) {
        log.debug("Verificando existencia de email en BD");
        return r2dbcRepository.existsByEmail(email);
    }
}