package exception;

public class NameUniqueViolationException extends Exception {
    public NameUniqueViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
