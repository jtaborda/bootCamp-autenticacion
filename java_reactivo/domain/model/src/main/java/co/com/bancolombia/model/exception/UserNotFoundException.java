package co.com.bancolombia.model.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String dato) {
        super(dato);
    }
}