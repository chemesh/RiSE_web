package data.util;

import java.text.NumberFormat;

public class UserActivityLog {

    private final String log;
    private final String newBalance;

    public UserActivityLog(UserAction type, String timestamp, int cost, int balance) {
        newBalance = NumberFormat.getNumberInstance().format(balance+cost);
        log = toString(type,timestamp,cost,balance,balance+cost);
    }

    public String getLog(){
        return log;
    }

    private String toString(UserAction type, String timestamp, int cost, int oldBalance, int newBalance) {

        return "Type: " + type +
                " | Timestamp: " + timestamp +
                (type.getSymbol()==null ? "" : " | Symbol: "+type.getSymbol())+
                " | Cost: " + NumberFormat.getNumberInstance().format(cost) +
                " | Old Balance: " + NumberFormat.getNumberInstance().format(oldBalance) +
                " | New Balance: " + NumberFormat.getNumberInstance().format(newBalance);
    }
}
