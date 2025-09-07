package co.com.crediya.r2dbc.rol;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolR2dbcRepository extends ReactiveCrudRepository<RolEntity, String> {
    Mono<RolEntity> findByNombreAndActive(String nombre, Boolean active);
}