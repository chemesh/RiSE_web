package data.user;

import data.stock.Stock;
import exception.InvalidStockSymbol;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.*;


public class Portfolio {

    User user;
    private Map<String,Item> holdings;
    private LongProperty totalWorth;
    private int creditBalance;

    public Portfolio (User user){
        this.holdings = new HashMap<>();
        totalWorth = new SimpleLongProperty(0);
        creditBalance = 0;
        this.user = user;
    }

    public long getTotalWorth() {
        return totalWorth.get();
    }

    public LongProperty totalWorthProperty() {
        return totalWorth;
    }

    public int getCreditBalance() {
        return creditBalance;
    }


    public Set<String> getStockSymbolsInPossession(){
        return holdings.keySet();
    }

    public void updateItem(Stock stock, int quantity){
        String symbol = stock.getSymbol();
        Item item = holdings.get(symbol);
        long ogWorth = 0;
        if (item != null){
            ogWorth = item.getWorth();
            item.quantityProperty().set(item.quantityProperty().get()+quantity);
            item.setWorth(item.getQuantity()*stock.getPrice());
        }
        else {
            item = new Item(stock, quantity);
            holdings.put(symbol,item);
        }
        int cost = (int) -(ogWorth-item.getWorth());
        totalWorth.set(totalWorth.get()+cost);
        //updateCredit(cost);
    }

    public Item getItem(String symbol) throws InvalidStockSymbol{
        symbol = symbol.toUpperCase();
        Item item = holdings.get(symbol);

        if (item == null){
            throw new InvalidStockSymbol(symbol);
        }
        return item;
    }

    public List<Item> getItemList(){
        return new ArrayList<>(holdings.values());
    }

    public void updateCredit(int amount) {
        this.creditBalance += amount;
    }

    public void setCredit(int amount) {
        creditBalance = 0;
    }
}
