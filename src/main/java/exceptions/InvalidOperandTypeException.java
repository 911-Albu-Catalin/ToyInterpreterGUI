package exceptions;

public class InvalidOperandTypeException extends RuntimeException {
    public InvalidOperandTypeException(String msg) {
        super(msg);
    }
}
