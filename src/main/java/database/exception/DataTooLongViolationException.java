package database.exception;

public class DataTooLongViolationException extends Exception {
    public DataTooLongViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
