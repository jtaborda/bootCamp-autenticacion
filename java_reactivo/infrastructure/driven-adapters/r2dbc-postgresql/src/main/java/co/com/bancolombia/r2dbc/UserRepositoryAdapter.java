package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.userEntity;
import co.com.bancolombia.r2dbc.exception.UserNotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        userEntity,
        Long,
        UserReactiveRepository
> implements UserRepository{
    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }


    @Override
    public Mono<User> saveUser(User user){
        return super.save(user);
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
                .flatMap(userEntity -> repository.deleteById(userEntity.getId().longValue()));
    }


    @Override
    public Mono<Boolean> getUserByCorreo(String correo) {
        return repository.findByCorreo(correo)
                .map(userEntity -> true)
                .defaultIfEmpty(false);
    }
}
