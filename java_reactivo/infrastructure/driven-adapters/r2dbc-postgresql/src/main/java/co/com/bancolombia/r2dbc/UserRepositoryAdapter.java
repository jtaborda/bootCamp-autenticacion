package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.userEntity;
import co.com.bancolombia.r2dbc.exception.UserNotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        userEntity,
        Long,
        UserReactiveRepository
> implements UserRepository{

    private final TransactionalOperator transactionalOperator;

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
        this.transactionalOperator = transactionalOperator;
    }


    @Override
    public Mono<User> saveUser(User user){
        return super.save(user)
         .as(transactionalOperator::transactional);//roolback
    }
    @Override
    public Flux<User> getAllUser() {
        return super.findAll();
    }

    @Override
    public Mono<User>getUserByIdNumber(Long id) {
        return super.findById(id);
    }

    @Override
    public Mono<Void> deleteUserById(Long id) {
        return repository.findById(id)                 .switchIfEmpty(Mono.error(new UserNotFoundException("No se encontró usuario ")))
                .flatMap(userEntity -> repository.deleteById(userEntity.getId().longValue()))
                .as(transactionalOperator::transactional);
    }


    @Override
    public Mono<Boolean> getUserByCorreo(String correo) {
        return repository.findByCorreo(correo)
                .map(userEntity -> true)
                .defaultIfEmpty(false);
    }
    @Override
    public Mono<User> findByDocumento(Long document) {
        return repository.findByDocumento(document)
                .map(entity -> mapper.map(entity, User.class));
    }
}
