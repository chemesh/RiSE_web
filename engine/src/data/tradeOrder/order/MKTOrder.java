package data.tradeOrder.order;


import data.stock.Stock;
import data.user.User;

public class MKTOrder extends Order {

    public MKTOrder(String action, Stock s, int quantity, User initiator)
    {
        super(action, s, quantity, s.getPrice(),initiator);
    }

    @Override
    public Order clone() {
        return new MKTOrder(this.action,this.stock,this.quantity,this.initiator);
    }
}
