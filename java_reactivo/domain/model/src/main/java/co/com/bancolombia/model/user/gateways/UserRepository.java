package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> saveUser(User user);
    Flux<User> getAllUser();
    Mono<Void> deleteUserById(Long id);
    Mono<Boolean> getUserByCorreo(String correo);
    Mono<User> findByDocumento(Long id);
    Mono<User> getUserById(Long id);
    Mono<User> findByCorreoAndPassword(String correo,String Documento);
}
