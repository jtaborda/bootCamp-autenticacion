package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.UserDto;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.usecase.user.UserUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {

    private final UserUseCase userUseCase;
    private final UserDTOMapper userMapper ;
    @PostMapping
    public Mono<ResponseEntity<Void>> createUser(@Valid @RequestBody CreateUserDto createUserDTO) {
        return userUseCase.saveUser(userMapper.toModel(createUserDTO))
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }

    @GetMapping
    public Flux<UserDto> getAllUser() {
        return userUseCase.getAllUser()
                .map(userMapper::toResponse);
    }

    @GetMapping("/{idNumber}")
    public Mono<ResponseEntity<UserDto>> getByIdNumber(@PathVariable("idNumber") long idNumber) {
        return userUseCase.getUserByIdNumber(idNumber)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{idNumber}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("idNumber") long idNumber) {
        return userUseCase.deleteUserByCorreo(idNumber)
                .then(Mono.just(ResponseEntity.ok().build()));
    }


}
