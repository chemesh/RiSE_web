package data.tradeOrder.list;

import data.stock.Stock;
import data.tradeOrder.order.CompletedOrder;
import data.tradeOrder.order.Order;
import data.user.User;
import data.util.TradeLog;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class CompletedTradesList {

    private ArrayList<CompletedOrder> list;
    private int version;



    public CompletedTradesList() {
        list = new ArrayList<>();
        version = 0;
    }

    public ArrayList<CompletedOrder> getList() {
        return list;
    }

    public void add(Order order, User acceptor) {
        list.add(new CompletedOrder(order,acceptor));
        version++;
    }

    public void add(CompletedOrder order) {
        list.add(order);
        version++;
    }

    public void add(Stock s, int quantity, int price, User initiator, User acceptor, boolean isInitiatorSeller) {
        list.add(new CompletedOrder(s,quantity,price,initiator,acceptor,isInitiatorSeller));
        version++;
    }

    public int getLength(){
        return list.size();
    }

    public int getCycle()
    {
        int sum =0;

        for (CompletedOrder o : list){
            sum += o.getCycle();
        }
        return sum;
    }

    @Override
    public String toString() {
        String s = "";
        int sum = 0;
        ListIterator<CompletedOrder> itr = list.listIterator(list.size());

        while(itr.hasPrevious()) {
            s=s.concat(itr.previous().toString()+"\n");
            itr.next();
            sum += itr.previous().getCycle();
        }

        if (s.isEmpty()) {
            s = "There are no Completed Trades yet\n";
        }
        s=s.concat("Total Business Cycle: "+Order.intToString(sum)+"\n");
        return s;
    }

    public List<TradeLog> getLogList(int startIndex) {
        List<TradeLog> res = new ArrayList<>();
        list.subList(startIndex,version).forEach((completedOrder)->{
            res.add(new TradeLog(completedOrder));
        });
        return res;
    }

    public int getLogVersion() {
        return version;
    }
}
