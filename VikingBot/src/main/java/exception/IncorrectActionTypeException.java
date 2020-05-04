package exception;

/**
 * The exception that is thrown when there's an unknown ActionType.
 */
public class IncorrectActionTypeException extends RuntimeException {
    public IncorrectActionTypeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public IncorrectActionTypeException(String errorMessage) {
        super(errorMessage);
    }
}
