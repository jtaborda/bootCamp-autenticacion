package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.r2dbc.entity.userEntity;
import co.com.bancolombia.r2dbc.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UserRepositoryAdapterTest {

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter adapter;
    @Mock
    private TransactionalOperator transactionalOperator;

    @BeforeEach
    void setup() {
        when(transactionalOperator.transactional(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        adapter = new UserRepositoryAdapter(repository, mapper, transactionalOperator);

    }

    @Test
    void saveUser_shouldReturnSavedUser() {
        User domainUser = new User(); 
        userEntity entity = new userEntity();
        entity.setId(1L);

        when(mapper.map(domainUser, userEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(domainUser);

        StepVerifier.create(adapter.saveUser(domainUser))
                .expectNext(domainUser)
                .verifyComplete();
    }

    @Test
    void getAllUser_shouldReturnAllUsers() {
        userEntity entity = new userEntity();
        User user = new User();

        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        StepVerifier.create(adapter.getAllUser())
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void getUserByIdNumber_shouldReturnUser() {
        long id = 1L;
        userEntity entity = new userEntity();
        entity.setId(id);
        User user = new User();

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        StepVerifier.create(adapter.getUserByIdNumber(id))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void deleteUserById_shouldDeleteUser() {
        long id = 1L;
        userEntity entity = new userEntity();
        entity.setId(id);

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(repository.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteUserById(id))
                .verifyComplete();

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deleteUserById_shouldThrowExceptionIfUserNotFound() {
        long id = 99L;

        when(repository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteUserById(id))
                .expectError(UserNotFoundException.class)
                .verify();

        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void getUserByCorreo_shouldReturnTrueIfExists() {
        String correo = "test@example.com";
        userEntity entity = new userEntity();

        when(repository.findByCorreo(correo)).thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.getUserByCorreo(correo))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void getUserByCorreo_shouldReturnFalseIfNotFound() {
        String correo = "noexiste@example.com";

        when(repository.findByCorreo(correo)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.getUserByCorreo(correo))
                .expectNext(false)
                .verifyComplete();
    }
}
