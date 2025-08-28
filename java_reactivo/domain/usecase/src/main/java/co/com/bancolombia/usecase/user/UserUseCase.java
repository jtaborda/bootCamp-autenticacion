package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.exception.EmailAlreadyExistsException;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class UserUseCase {


    private final UserRepository userRepository;
    public Mono<Void> saveUser(User user) {
        return userRepository.getUserByCorreo(user.getCorreo())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new EmailAlreadyExistsException(
                                "El correo ya está registrado: " + user.getCorreo()));
                    }

                    return userRepository.saveUser(user).then();
                });
    }

    public Flux<User> getAllUser()
    {
        return userRepository.getAllUser();
    }
    public Mono<User> getUserByIdNumber(Long idNumber){
        return userRepository.getUserByIdNumber(idNumber);
    }
    public Mono<Void> deleteUserByCorreo(Long idNumber)
    {
        return userRepository.deleteUserById(idNumber);
    }

    public Mono<User> getUserByDocumento(Long idNumber){
        return userRepository.findByDocumento(idNumber);
    }
}
