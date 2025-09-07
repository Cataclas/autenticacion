package co.com.crediya.model.auth.exceptions;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}