package exceptions;

public class InvalidOperationTypeException extends RuntimeException {
    public InvalidOperationTypeException(String msg) {
        super(msg);
    }
}
