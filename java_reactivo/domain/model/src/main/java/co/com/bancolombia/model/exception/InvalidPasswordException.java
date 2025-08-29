package co.com.bancolombia.model.exception;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException(String dato) {
        super(dato);
    }
}