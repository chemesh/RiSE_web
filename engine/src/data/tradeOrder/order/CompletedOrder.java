package data.tradeOrder.order;

import data.stock.Stock;
import data.user.User;

public class CompletedOrder extends Order{
    private final User acceptor;
    private final int cycle;
    private final boolean isInitiatorSeller;

    public CompletedOrder(Stock s, int quantity, int price, User initiator, User acceptor, boolean isInitiatorSeller)
    {
        super("COMPLETE",s,quantity,price,initiator );
        cycle = price * quantity;
        this.acceptor = acceptor;
        this.isInitiatorSeller = isInitiatorSeller;
    }

    public CompletedOrder(Order order, User acceptor)
    {
        super("COMPLETED", order.getStock(),
                order.getQuantity(), order.getPrice(), order.initiator);
        cycle = order.getPrice() * order.getQuantity();
        this.acceptor = acceptor;
        if (order.isSelling()) {
            isInitiatorSeller = true;
        }
        else isInitiatorSeller = false;
    }

    public int getCycle() {
        return cycle;
    }

    @Override
    public String toString(){
        String seller = acceptor.getName(), buyer = initiator.getName();
        if (isInitiatorSeller){
            seller = buyer;         //swap
            buyer = acceptor.getName();
        }
        return("Time: " + timestamp +
                " , Stock Amount: " + intToString(quantity) +
                " , Price per Share: " + intToString(price) +
                " , Total Trade Price: "+intToString(cycle) +
                " , Seller Username: " + seller +
                " , Buyer Username: " + buyer);
    }

    @Override
    public Order clone() {
        return new CompletedOrder(this,acceptor);
    }

}
