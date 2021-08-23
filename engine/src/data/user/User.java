package data.user;

import data.stock.Stock;
import data.tradeOrder.order.Order;
import data.util.UserAction;
import data.util.UserActivityLog;
import generated.RseUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private int role;
    private Portfolio portfolio;
    private List<UserActivityLog> userLogs;
    private int userLogsVersion;

    public static final int ROLE_ADMIN = 0;
    public static final int ROLE_TRADER = 1;
    private static final String ADMIN = "admin";
    private static final String TRADER = "trader";



    public User(String name, int role ){
        this.name = name;
        this.role = role;
        if (role == ROLE_ADMIN)
            portfolio = null;
        else
            portfolio = new Portfolio(this);
        userLogs = new ArrayList<>();
        userLogsVersion=0;
    }

    public User(RseUser u){
        this(u.getName(),ROLE_TRADER);
    }

    public User(String name, String role){
        this(name);
        if (role.equalsIgnoreCase(ADMIN)) {
            this.role = ROLE_ADMIN;
            this.portfolio = null;
        }
    }

    public User(String name){
        this(name,ROLE_TRADER);
    }

    public void addNewActivityLog(UserAction type,String time,int cost,int balance){
        userLogs.add(new UserActivityLog(type,time,cost,balance));
        userLogsVersion++;
    }



    public Portfolio getPortfolio(){
        return portfolio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        if (this.role == ROLE_ADMIN)
            return ADMIN;
        return TRADER;
    }

    public void chargeCredit(int amount){
        String timestamp = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS").format(LocalDateTime.now());
        addNewActivityLog(UserAction.Charge,timestamp,amount, portfolio.getCreditBalance());
        this.portfolio.updateCredit(amount);
    }

    public int getCreditBalance() {
        return portfolio.getCreditBalance();
    }

    public void updateItem(Stock stock, int tradeQuantity, Order newTrade) {
        UserAction type= newTrade.isSelling() ? UserAction.Sell : UserAction.Buy;
        type.setSymbol(stock.getSymbol());
        int cost = newTrade.getPrice() * -1;
        cost*=tradeQuantity;
        addNewActivityLog(type, newTrade.timestamp, cost,getCreditBalance());
        portfolio.updateItem(stock,tradeQuantity);
        portfolio.updateCredit(cost);
    }

    public void updateItemOnUpload(Stock stock, int quantity){
        int credit = getCreditBalance();
        portfolio.updateItem(stock,quantity);
        portfolio.setCredit(credit);
    }

    public List<UserActivityLog> getUserLogs(int clientVersion) {
        return userLogs.subList(clientVersion,userLogsVersion);
    }

    public int getActivityLogVersion() {
        return userLogsVersion;
    }
}
