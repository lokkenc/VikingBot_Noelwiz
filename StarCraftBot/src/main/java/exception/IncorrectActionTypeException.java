package exception;

public class IncorrectActionTypeException extends RuntimeException {
    public IncorrectActionTypeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public IncorrectActionTypeException(String errorMessage) {
        super(errorMessage);
    }
}
