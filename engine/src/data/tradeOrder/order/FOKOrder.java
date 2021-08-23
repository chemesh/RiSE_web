package data.tradeOrder.order;

import data.stock.Stock;
import data.user.User;

public class FOKOrder extends Order{

    public FOKOrder(String action, Stock s, int quantity, int price, User initiator) {
        super(action, s, quantity, price, initiator);
    }

    @Override
    public Order clone() {
        return new FOKOrder(this.action,this.stock,this.quantity,this.price,this.initiator);
    }
}
