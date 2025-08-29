package co.com.bancolombia.api;


import co.com.bancolombia.api.dto.LoginDto;
import co.com.bancolombia.api.mapper.UserDTOMapper;
import co.com.bancolombia.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import co.com.bancolombia.api.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Usuarios", description = "API para manejo de Inicio de Sesion")
@RestController
@RequestMapping(value = "/api/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRestLogin {

    private static final Logger logger = LoggerFactory.getLogger(ApiRestLogin.class);
    private final UserUseCase userUseCase;
    private final UserDTOMapper userMapper ;
    private final JwtUtil jwtUtil;

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody LoginDto loginDto) {
        return userUseCase.findByCorreoAndPassword(loginDto.correo(), loginDto.password())
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getCorreo(), user.getNombreRol(),user.getDocumento());

                    Map<String, Object> response = new HashMap<>();
                    response.put("user", userMapper.toResponse(user));
                    response.put("token", token);

                    return ResponseEntity.ok(response);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(401).build()));
    }
}
