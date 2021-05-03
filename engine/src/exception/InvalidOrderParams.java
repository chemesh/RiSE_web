package exception;

public class InvalidOrderParams extends RuntimeException{

    public InvalidOrderParams(String msg) {
        super(msg);
    }
}
