package data.tradeOrder.order;

import data.stock.Stock;
import data.user.User;
import exception.InvalidOrderParams;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Order implements Cloneable {

    protected String action;
    protected final Stock stock;
    protected Integer quantity;
    protected final Integer price;
    public final String timestamp;
    protected final User initiator;
    private boolean pending;


    public User getUser() {
        return initiator;
    }

    public Order(String action, Stock s, int quantity, int price, User initiator)
    {
        this.quantity = quantity;
        this.stock = s;
        this.price = price;
        this.initiator = initiator;
        if (action.equalsIgnoreCase("s")||action.equalsIgnoreCase("sell")) {
            this.action = "sell";
            //updateInitiatorItemAmountOnSelling(s, quantity);
        }
        else
            this.action = "buy";
        timestamp = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS").format(LocalDateTime.now());
        pending = false;

    }

    public void updateInitiatorItemAmountOnSelling(Stock s, int quantity){
        //initiator.getPortfolio().updateItem(s, quantity*-1);
        initiator.updateItem(s,-1*quantity,this);
    }

    public int getPrice() {
        return price;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAction(){
        return action;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelling(){
        return (action.equalsIgnoreCase("sell"));
    }

    public boolean isBuying() { return action.equals("buy"); }

    @Override
    public String toString(){
        return ("Time: " + timestamp +
                " , Action: " + action +
                " , SYMBOL: " + stock.symbol +
                " , Order Type: " + getOrderType() +
                " , Quantity: " + intToString(quantity) +
                " , Price Limit: " + intToString(price) +
                " , Trade worth: " + intToString(price * quantity)) +
                " , Initiator Username: "+initiator.getName();
    }

    public boolean equals(Order order) {
        return (super.equals(order)
                && timestamp.equals(order.timestamp)
                && price == order.price
                && action.equals(order.action)
                && stock.equals(stock)
                && quantity == order.quantity);
    }

    public static String intToString(Integer sum){

        int sumLength = sum.toString().length();
        if (sumLength < 4)
            return sum.toString();

        StringBuilder res = new StringBuilder();
        for (int i = 0 ; i < sumLength ; i++){
            if (i % 3 == 0 && i != 0){
                res.insert(0,",");
            }
            res.insert(0,sum%10);
            sum/=10;
        }
        return res.toString();
    }

    public String getOrderType(){
        Class type = this.getClass();
        if (this instanceof MKTOrder)
            return "MKT";
        else if(this instanceof LMTOrder)
            return "LMT";
        else if(this instanceof FOKOrder)
            return "FOK";
        else if (this instanceof IOCOrder)
            return "IOC";
        else
            throw new InvalidOrderParams("bad order. type is " + this.getClass().getSimpleName());
    }

    public abstract Order clone();

    public void setPending(){
        this.pending = true;
    }

    public void unPend(){
        this.pending = false;
    }

    public boolean isPending(){
        return this.pending;
    }
}






