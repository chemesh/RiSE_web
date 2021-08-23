package exception;

public class InvalidCapacityException extends Exception {
    public InvalidCapacityException() {}

    public InvalidCapacityException(String message) {
        super(message);
    }
}
