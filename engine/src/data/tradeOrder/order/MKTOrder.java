package data.tradeOrder.order;


import data.stock.Stock;

public class MKTOrder extends Order {

    public MKTOrder(String action, Stock s, int quantity)
    {
        super(action, s, quantity, s.getPrice());
    }

    @Override
    public Order clone() {
        return new MKTOrder(this.action,this.stock,this.quantity);
    }
}
