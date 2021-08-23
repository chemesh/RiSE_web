package data.util;

public enum UserAction {
    Charge, Buy, Sell;
    private String symbol;

    UserAction(String symbol){
        this.symbol = symbol;
    }
    UserAction() {
    }

    public void setSymbol(String sym){
        if (this!=Charge)
            symbol = sym;
    }

    public String getSymbol() {
        return symbol;
    }
}


