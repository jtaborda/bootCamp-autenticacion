package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.rolEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolReactiveRepository
        extends ReactiveCrudRepository
        <rolEntity, Long>, ReactiveQueryByExampleExecutor<rolEntity>
{

    Mono<rolEntity> findById(Long id);

}
