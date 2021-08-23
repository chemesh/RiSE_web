package data.util;

import data.tradeOrder.order.Order;

public class TradeLog {
    private final String log;

    public TradeLog(Order order) {
        this.log = order.toString();
    }
    public String getLog(){
        return log;
    }
}
