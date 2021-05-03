package data.tradeOrder.order;

import data.stock.Stock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Order implements Cloneable {

    protected String action;
    protected final Stock stock;
    protected Integer quantity;
    protected  final  Integer price;
    public final String timestamp;

    public Order(String action, Stock s, int quantity, int price)
    {
        if (action.equalsIgnoreCase("s"))
            this.action = "sell";
        else
            this.action = "buy";
        this.quantity = quantity;
        this.stock = s;
        this.price = price;
        timestamp = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS").format(LocalDateTime.now());
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
        return (action.equals("sell"));
    }

    @Override
    public String toString(){
        return ("Time: "+timestamp+" , Action: "+action+" , SYMBOL: "+stock.symbol+
                " , Quantity: "+intToString(quantity)+" , Price Limit: "+intToString(price)+
                " , Trade worth: "+intToString(price*quantity));
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

    public abstract Order clone();
}






