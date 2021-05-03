package data.tradeOrder.order;

import java.util.ArrayList;
import java.util.List;

public class TradeDTO {
    private List<CompletedOrder> trades;
    private final Order originalOrder;
    private int quantityLeft;

    public TradeDTO(Order originalOrder, int stocksLeft)
    {
        trades = new ArrayList<>();
        this.originalOrder = originalOrder;
        quantityLeft = stocksLeft;
    }

    public TradeDTO(Order originalOrder)
    {
        trades = new ArrayList<>();
        this.originalOrder = originalOrder;
        quantityLeft = 0;
    }

    public Order getOriginalOrder() {
        return originalOrder;
    }

    public int getQuantityLeft(){
        return quantityLeft;
    }

    public void setQuantityLeft(int quantity){
        quantityLeft = quantity;
    }

    public boolean isOrderFullyMade(){
        return (quantityLeft == 0);
    }

    public boolean isOrderPartiallyMade(){
        return (originalOrder.quantity > quantityLeft);
    }

    public boolean isEmpty()
    {
        return (trades.size()==0);
    }

    public List<CompletedOrder> getTrades(){
        if (trades.size() == 0)
            return null;
        return trades;
    }

    public void add(CompletedOrder newTrade) {
        trades.add(newTrade);
    }

    @Override
    public String toString() {
        String s ="Trade Order made: \n" +
                originalOrder.toString()+"\n" +
                "Stock Quantity left to complete: "+Order.intToString(quantityLeft)+"\n" +
                "Trades Completed:\n\n";

        for (CompletedOrder t : trades)
        {
            s=s.concat(t.toString()+'\n');
        }
        return s;
    }
}
