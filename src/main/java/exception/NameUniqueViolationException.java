package exception;

public class NameUniqueViolationException extends Exception {
    public NameUniqueViolationException() {
        super();
    }

    public NameUniqueViolationException(String message) {
        super(message);
    }

    public NameUniqueViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameUniqueViolationException(Throwable cause) {
        super(cause);
    }
}
