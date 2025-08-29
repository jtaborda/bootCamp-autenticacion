package co.com.bancolombia.model.exception;

public class InvalidJwtException extends RuntimeException{

    public InvalidJwtException(String s) {
        super(s);
    }
}