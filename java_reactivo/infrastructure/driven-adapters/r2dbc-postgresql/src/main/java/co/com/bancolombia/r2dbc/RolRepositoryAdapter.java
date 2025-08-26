package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.rol.Rol;
import co.com.bancolombia.model.rol.gateways.RolRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.rolEntity;
import co.com.bancolombia.r2dbc.entity.userEntity;
import co.com.bancolombia.r2dbc.exception.UserNotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RolRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        rolEntity,
        Long,
        RolReactiveRepository
> implements RolRepository {

    private final TransactionalOperator transactionalOperator;
    private static final Logger logger = LoggerFactory.getLogger(RolRepositoryAdapter.class);
    public RolRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity, Rol.class));
        this.transactionalOperator = transactionalOperator;
    }

}
