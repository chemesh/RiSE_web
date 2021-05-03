package exception;

public class InvalidActionException extends Exception {


    @Override
    public String getMessage() {
        return "INVALID INPUT!\n" +
                "please enter a legal number, representing the operation desired";
    }
}
