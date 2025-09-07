package co.com.crediya.model.usuario.gateways;

import co.com.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<Usuario> guardar(Usuario usuario);
    Mono<Boolean> existePorEmail(String email);
    Mono<Usuario> findByEmailAndActive(String email, Boolean active);
    Mono<Usuario> findById(String idUsuario);
}
