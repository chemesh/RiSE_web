package data.tradeOrder.order;

import data.stock.Stock;

public class LMTOrder extends Order{

    public LMTOrder(String action, Stock s, int quantity, int price)
    {
        super(action, s, quantity, price);
    }

    @Override
    public Order clone() {
        return new LMTOrder(this.action,this.stock,this.quantity,this.price);
    }

}


