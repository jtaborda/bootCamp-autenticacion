package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.UserDto;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.api.security.JwtUtil;
import co.com.bancolombia.model.exception.InvalidJwtException;
import co.com.bancolombia.model.exception.UserNotFoundException;
import co.com.bancolombia.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Usuarios", description = "API para manejo de usuarios")
@RestController
@RequestMapping(value = "/api/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {

    private static final Logger logger = LoggerFactory.getLogger(ApiRest.class);
    private final UserUseCase userUseCase;
    private final UserDTOMapper userMapper ;
    private final JwtUtil jwtUtil;


    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping
    public Mono<ResponseEntity<Void>> createUser(
            @Valid @RequestBody CreateUserDto createUserDTO,
            @RequestHeader("Authorization") String authHeader) {

        logger.info("******************************-*");
        logger.info("Creando Usuario");
        logger.info("************************");

        String token = authHeader.replace("Bearer ", "");
        String nombreRol = jwtUtil.extractRol(token);

        if (!"Administrador".equalsIgnoreCase(nombreRol) && !"Asesor".equalsIgnoreCase(nombreRol)) {
            return Mono.error(new InvalidJwtException("Solo admin o Asesor, pueden crear un usuario"));
        }

        return userUseCase.saveUser(userMapper.toModel(createUserDTO))
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public Flux<UserDto> getAllUser() {
        logger.info("***************************");
        logger.info("Trayendo los usuarios");
        logger.info("*****************************");
        return userUseCase.getAllUser()
                .map(userMapper::toResponse);
    }

    @Operation(summary = "Obtener usuario por número de Id")
    @GetMapping("/id/{idNumber}")
    public Mono<ResponseEntity<UserDto>> getByIdNumber(@PathVariable("idNumber") long idNumber) {
        logger.info("********************-*");
        logger.info("Trayendo usuario por Id");
        logger.info("***********************--*");
        return userUseCase.getUserByIdNumber(idNumber)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(summary = "Eliminar usuario por número de identificación")
    @DeleteMapping("/{idNumber}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("idNumber") long idNumber) {
        return userUseCase.deleteUserByCorreo(idNumber)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @Operation(summary = "Obtener usuario por Documento")
    @GetMapping("/{documento}")
    public Mono<ResponseEntity<UserDto>> getByDocumento(@PathVariable("documento") long documento) {
           logger.info("Trayendo usuario por Documento "); logger.info("*********************-**--*");
        return userUseCase.getUserByDocumento(documento)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

}
