package exception;

public class DataTooLongViolationException extends Exception {
    public DataTooLongViolationException() {
        super();
    }

    public DataTooLongViolationException(String message) {
        super(message);
    }

    public DataTooLongViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataTooLongViolationException(Throwable cause) {
        super(cause);
    }
}
