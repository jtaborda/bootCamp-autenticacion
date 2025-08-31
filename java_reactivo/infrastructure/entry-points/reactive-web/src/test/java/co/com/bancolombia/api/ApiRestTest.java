package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.UserDto;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.api.security.JwtUtil;
import co.com.bancolombia.model.exception.InvalidJwtException;
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

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldReturnCreated_WhenUserIsAuthorized() {
        // Arrange
        CreateUserDto createUserDto = new CreateUserDto(
                "Juan", "Perez", "1990-01-01", 123456789, "Calle 123", "juan@example.com", 5000000L, 500L, 1L);

        User user = new User();
        String token = "mockedToken";
        String authHeader = "Bearer " + token;

        when(userMapper.toModel(createUserDto)).thenReturn(user);
        when(userUseCase.saveUser(user)).thenReturn(Mono.empty());
        when(jwtUtil.extractRol(token)).thenReturn("Administrador");

        // Act
        Mono<ResponseEntity<Void>> response = apiRest.createUser(createUserDto, authHeader);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(resp -> resp.getStatusCode() == HttpStatus.CREATED)
                .verifyComplete();

        verify(userUseCase, times(1)).saveUser(user);
        verify(jwtUtil, times(1)).extractRol(token);
    }

    @Test
    void createUser_ShouldReturnError_WhenUserIsNotAuthorized() {
        // Arrange
        CreateUserDto createUserDto = new CreateUserDto(
                "Juan", "Perez", "1990-01-01", 123456789, "Calle 123", "juan@example.com", 5000000L, 500L, 1L);

        String token = "mockedToken";
        String authHeader = "Bearer " + token;

        when(jwtUtil.extractRol(token)).thenReturn("Usuario"); // rol no permitido

        // Act
        Mono<ResponseEntity<Void>> response = apiRest.createUser(createUserDto, authHeader);

        // Assert
        StepVerifier.create(response)
                .expectError(InvalidJwtException.class)
                .verify();

        verify(userUseCase, never()).saveUser(any());
        verify(jwtUtil, times(1)).extractRol(token);
    }

    // se esta cuando exista correo
    @Test
    void getAllUser_ShouldReturnUsers() {
        User user = new User();
        UserDto userDto = new UserDto(1L, "Andre", "perez", "01/01/2021", 11, "calle123", "juan@example.com",500L,1111L,1L,"Administrador");

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
        UserDto userDto = new UserDto(1L, "Andre", "perez", "01/01/2021", 11, "calle123", "juan@example.com",500L,1111L,1L,"Administrador");

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
