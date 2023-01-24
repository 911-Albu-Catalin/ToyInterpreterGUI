package exceptions;

public class DivisionByZeroException extends RuntimeException {
    public DivisionByZeroException(String msg) {
        super(msg);
    }
}
