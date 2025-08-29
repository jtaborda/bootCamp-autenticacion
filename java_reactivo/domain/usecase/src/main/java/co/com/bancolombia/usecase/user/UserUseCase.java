package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.exception.EmailAlreadyExistsException;
import co.com.bancolombia.model.exception.InvalidPasswordException;
import co.com.bancolombia.model.exception.UserNotFoundException;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class UserUseCase {


    private final UserRepository userRepository;

    public Mono<Void> saveUser(User user) {
        return userRepository.getUserByCorreo(user.getCorreo().toLowerCase())
                .flatMap(correoExiste -> {
                    if (correoExiste) {
                        return Mono.error(new EmailAlreadyExistsException(
                                "El correo ya está registrado: " + user.getCorreo()));
                    }
                    return userRepository.findByDocumento(user.getDocumento())
                            .flatMap(documentoExiste ->
                                    Mono.error(new EmailAlreadyExistsException(
                                            "El documento ya está registrado: " + user.getDocumento()))
                            )
                            .switchIfEmpty(userRepository.saveUser(user).then());
                }).then();
    }

    public Flux<User> getAllUser()
    {
        return userRepository.getAllUser();
    }
    public Mono<User> getUserByIdNumber(Long idNumber){
        return userRepository.getUserById(idNumber)
                .switchIfEmpty(Mono.error(new UserNotFoundException("El usuario no existe")))
                .map(user -> user);
    }
    public Mono<Void> deleteUserByCorreo(Long idNumber)
    {
        return userRepository.deleteUserById(idNumber);
    }

    public Mono<User> getUserByDocumento(Long idNumber) {
        return userRepository.findByDocumento(idNumber)
                .switchIfEmpty(Mono.error(new UserNotFoundException("El usuario no existe")))
                .map(user -> user);
    }


    public Mono<User> findByCorreoAndPassword(String correo, String password) {
        return userRepository.findByCorreoAndPassword(correo.toLowerCase(), password)
                .switchIfEmpty(Mono.error(new UserNotFoundException(
                        "Usuario o Password Incorrectos")));
    }


}
