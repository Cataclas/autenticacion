package co.com.crediya.model.rol.gateways;

import co.com.crediya.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {
    Mono<Rol> findByNombre(String nombre);
    Mono<Rol> findById(String idRol);
}
