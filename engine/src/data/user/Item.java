package data.user;


import data.stock.Stock;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Item {
    private Stock stock;
    private IntegerProperty quantity;
    private IntegerProperty worth;

    public int getWorth() {
        return worth.get();
    }

    public IntegerProperty worthProperty() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth.set(worth);
    }

    public Item (Stock stock, int quantity){
        this.stock = stock;
        this.quantity = new SimpleIntegerProperty(quantity);
        this.worth = new SimpleIntegerProperty(quantity*stock.getPrice());
    }

    public Stock getStock(){
        return stock;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }
}
