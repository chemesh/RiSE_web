package data.tradeOrder.order;

import data.user.User;

import java.util.ArrayList;
import java.util.List;

public class TradeDTO {
    private List<CompletedOrder> trades;
    private final Order originalOrder;
    private User initiator;
    private User acceptor;
    private int quantityLeft;

    public TradeDTO(Order originalOrder, int stocksLeft)
    {
        this.initiator = originalOrder.getUser();
        trades = new ArrayList<>();
        this.originalOrder = originalOrder;
        quantityLeft = stocksLeft;
    }

    public TradeDTO(Order originalOrder)
    {
        this.initiator = originalOrder.getUser();
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
        String s ="Trade Order made by "+originalOrder.initiator.getName() +":\n" +
                originalOrder.toString()+"\n" +
                "Amount of stocks left to complete the Order: "+Order.intToString(quantityLeft)+"\n" +
                "Trades Completed:\n\n";
        String tradesLeft= "";
        for (CompletedOrder t : trades)
        {
            tradesLeft = tradesLeft.concat(t.toString()+'\n');
        }
        if (tradesLeft.equals(""))
            tradesLeft = "0";
        s = s.concat(tradesLeft);
        return s;
    }
}
