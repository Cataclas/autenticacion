package co.com.crediya.r2dbc.rol;

import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.rol.gateways.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RolRepositoryAdapter implements RolRepository {
    
    private final RolR2dbcRepository r2dbcRepository;
    
    @Override
    public Mono<Rol> findByNombre(String nombre) {
        return r2dbcRepository.findByNombreAndActive(nombre, true)
                .map(this::toDomain);
    }
    
    @Override
    public Mono<Rol> findById(String idRol) {
        return r2dbcRepository.findById(idRol)
                .map(this::toDomain);
    }
    
    private Rol toDomain(RolEntity entity) {
        return Rol.builder()
                .idRol(entity.getIdRol())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .active(entity.getActive())
                .build();
    }
}