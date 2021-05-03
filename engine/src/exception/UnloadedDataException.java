package exception;

public class UnloadedDataException extends Exception{

    @Override
    public String getMessage() {
        return ("ERROR! :There is no loaded data in the system.\n" +
                "Please Load a XML data file first using option 1 in the menu\n");
    }
}
