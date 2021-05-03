package data.tradeOrder.list;

import data.stock.Stock;
import data.tradeOrder.order.CompletedOrder;
import data.tradeOrder.order.Order;

import java.util.ArrayList;
import java.util.ListIterator;


public class CompletedTradesList {

    private ArrayList<CompletedOrder> list;



    public CompletedTradesList()
    {
        list = new ArrayList<>();
    }

    public ArrayList<CompletedOrder> getList() {
        return list;
    }

    public void add(Order order)
    {
        list.add(new CompletedOrder(order));
    }

    public void add(CompletedOrder order) {
        list.add(order);
    }

    public void add(Stock s, int quantity, int price)
    {
        list.add(new CompletedOrder(s,quantity,price));
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
        s=s.concat("Total Business Cycle: "+Order.intToString(sum)+"\n");

        if (s.isEmpty()) {
            return "There are no Completed Trades yet";
        }
        return s;
    }
}
