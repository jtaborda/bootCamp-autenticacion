package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.userEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository
        extends ReactiveCrudRepository
        <userEntity, Long>, ReactiveQueryByExampleExecutor<userEntity>
{
    Mono<userEntity> findByCorreo(String correo);
    Mono<userEntity> findByDocumento(Long document);
    Mono<userEntity> findById(Long id);
    Mono<userEntity> findByCorreoAndPassword(String correo, String password);
}
