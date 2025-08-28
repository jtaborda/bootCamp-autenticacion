package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.UserDto;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.Mockito.*;

class ApiRestTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private UserDTOMapper userMapper;

    @InjectMocks
    private ApiRest apiRest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldReturnCreated() {
        CreateUserDto createUserDto = new CreateUserDto(
                "Juan", "Perez", "1990-01-01", 123456789, "Calle 123", "juan@example.com", 5000000L,500L,1L);

        User user = new User();

        when(userMapper.toModel(createUserDto)).thenReturn(user);
        when(userUseCase.saveUser(user)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Void>> response = apiRest.createUser(createUserDto);

        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.getStatusCode() == HttpStatus.CREATED)
                .verifyComplete();

        verify(userUseCase, times(1)).saveUser(user);
    }
// se esta cuando exista correo
    @Test
    void getAllUser_ShouldReturnUsers() {
        User user = new User();
        UserDto userDto = new UserDto("Juan", "Perez", "1990-01-01", 123456789, "Calle 123", "juan@example.com", 5000000L,500L,1L,"Administrador");

        when(userUseCase.getAllUser()).thenReturn(Flux.just(user));
        when(userMapper.toResponse(user)).thenReturn(userDto);

        Flux<UserDto> response = apiRest.getAllUser();

        StepVerifier.create(response)
                .expectNext(userDto)
                .verifyComplete();

        verify(userUseCase, times(1)).getAllUser();
    }

    @Test
    void getByIdNumber_UserExists_ShouldReturnUser() {
        long idNumber = 123L;
        User user = new User();
        UserDto userDto = new UserDto("Juan", "Perez", "1990-01-01", 123456789, "Calle 123", "juan@example.com", 5000000L,500L,1L,"Administrador");

        when(userUseCase.getUserByIdNumber(idNumber)).thenReturn(Mono.just(user));
        when(userMapper.toResponse(user)).thenReturn(userDto);

        Mono<ResponseEntity<UserDto>> response = apiRest.getByIdNumber(idNumber);

        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.getStatusCode() == HttpStatus.OK && resp.getBody().equals(userDto))
                .verifyComplete();

        verify(userUseCase, times(1)).getUserByIdNumber(idNumber);
    }

    @Test
    void getByIdNumber_UserNotFound_ShouldReturnNotFound() {
        long idNumber = 123L;

        when(userUseCase.getUserByIdNumber(idNumber)).thenReturn(Mono.empty());

        Mono<ResponseEntity<UserDto>> response = apiRest.getByIdNumber(idNumber);

        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.getStatusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(userUseCase, times(1)).getUserByIdNumber(idNumber);
    }

    @Test
    void deleteUser_ShouldReturnOk() {
        long idNumber = 123L;

        when(userUseCase.deleteUserByCorreo(idNumber)).thenReturn(Mono.empty());

        Mono<ResponseEntity<Void>> response = apiRest.deleteUser(idNumber);

        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.getStatusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(userUseCase, times(1)).deleteUserByCorreo(idNumber);
    }
}
