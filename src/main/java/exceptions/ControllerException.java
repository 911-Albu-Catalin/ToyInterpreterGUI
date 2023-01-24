package exceptions;

public class ControllerException extends RuntimeException {
    public ControllerException(String msg) {
        super(msg);
    }
}
