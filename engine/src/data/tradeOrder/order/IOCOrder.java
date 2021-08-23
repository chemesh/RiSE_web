package data.tradeOrder.order;

import data.stock.Stock;
import data.user.User;

public class IOCOrder extends Order{
    public IOCOrder(String action, Stock s, int quantity, int price, User initiator) {
        super(action, s, quantity, price, initiator);
    }

    @Override
    public Order clone() {
        return new IOCOrder(this.action,this.stock,this.quantity,this.price,this.initiator);
    }
}
