package exception;

public class InvalidStockSymbol extends Exception {

    String input;


    public InvalidStockSymbol(String input) {
        this.input = input;
    }

    @Override
    public String getMessage() {
        return ("Error! Stock symbol '" + input + "' doesn't exist in the system");
    }

}
