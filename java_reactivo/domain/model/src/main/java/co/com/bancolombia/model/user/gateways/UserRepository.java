package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> saveUser(User user);

    Flux<User> getAllUser();

    Mono<User>  getUserByIdNumber(Long idNumber);

    Mono<Void> deleteUserById(Long id);
    Mono<Boolean>  getUserByCorreo(String correo);
}
