package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.rol.gateways.RolRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
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
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        userEntity,
        Long,
        UserReactiveRepository
> implements UserRepository{

    private final RolRepository rolRepository;
    private final TransactionalOperator transactionalOperator;


    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryAdapter.class);
    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, RolRepository rolRepository, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
        this.rolRepository = rolRepository;
        this.transactionalOperator = transactionalOperator;
    }


    @Override
    public Mono<User> saveUser(User user){
        return super.save(user)
         .as(transactionalOperator::transactional);//roolback
    }
    @Override
    public Flux<User> getAllUser()
    {
         return repository.findAll()
                 .flatMap(entity ->
                                 rolRepository.findById(entity.getIdRol())
                     .map( rol-> new User(
                             entity.getNombre(),
                             entity.getApellido(),
                             entity.getFechaNacimiento(),
                             entity.getTelefono(),
                             entity.getDireccion(),
                             entity.getCorreo(),
                             entity.getSalario(),
                             entity.getDocumento(),
                             rol.getId_rol(),
                             rol.getNombre()
                     ))

                 );
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
        logger.info("********************-*"); logger.info("Trayendo usuario por Documento"); logger.info("***********************--*");
        return repository.findByDocumento(document)
                .map(entity -> mapper.map(entity, User.class));
    }
}
