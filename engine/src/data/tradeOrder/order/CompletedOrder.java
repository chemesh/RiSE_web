package data.tradeOrder.order;

import data.stock.Stock;

public class CompletedOrder extends Order{
    private final int cycle;

    public CompletedOrder(Stock s, int quantity, int price)
    {
        super("COMPLETE",s,quantity,price );
        cycle = price * quantity;
    }

    public CompletedOrder(Order order)
    {
        super("COMPLETED", order.getStock(),
                order.getQuantity(), order.getPrice());
        cycle = order.getPrice() * order.getQuantity();
    }

    public int getCycle() {
        return cycle;
    }

    @Override
    public String toString(){
        return("Time: "+timestamp+" , Stock Quantity: "+intToString(quantity)+" , Price per Share: " +
                intToString(price)+" , Total Trade Price: "+intToString(cycle));
    }

    @Override
    public Order clone() {
        return new CompletedOrder(this);
    }
}
