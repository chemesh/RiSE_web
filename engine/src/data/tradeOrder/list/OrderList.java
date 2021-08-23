package data.tradeOrder.list;

import data.tradeOrder.order.MKTOrder;
import data.tradeOrder.order.Order;
import data.util.TradeLog;

import java.util.*;

public class OrderList {

    private TreeMap<Integer, List<Order>> list;

    public OrderList(){
        list = new TreeMap<>();

    }

    public void addOrder (Order o){
       // if (o instanceof MKTOrder)
            //o = new LMTOrder(o.getAction(),o.getStock(),o.getQuantity(),o.getStock().getPrice(), o.getUser());
        if(list.containsKey(o.getPrice())){
            list.get(o.getPrice()).add(o);
        }
        else{
            List<Order> subList = new LinkedList<>();
            subList.add(o);
            list.put(o.getPrice() ,subList);

        }
    }

    public void removeOrder(Order o) {
        list.get(o.getPrice()).remove(o);
        if (list.get(o.getPrice()).isEmpty()){
            list.remove(o.getPrice());
        }
    }

    //returns a map using price as key and a list of trade orders with that price as value
    public Map<Integer, List<Order>> getAllTradeOrders()
    {
        return list;
    }

    public List<Order> getAllTradeOrdersAsList(){
        List<Order> allOrders = new ArrayList<>();

        for (Map.Entry<Integer,List<Order>> listEntry : list.entrySet()){
            allOrders.addAll(getTradeOrderListByPrice(listEntry.getKey()));
        }
        return allOrders;
    }

    //receive an int and returns a sorted list (by entry time) of orders with that price
    public List<Order> getTradeOrderListByPrice(int price)
    {
        List<Order> oList = list.get(price);
        if (oList != null && !(oList.isEmpty())){
            List<Order> finalResList = new ArrayList<>();
            oList.forEach((order -> {
                if (!(order.isPending()))
                    finalResList.add(order);
            }));
            if (!(finalResList.isEmpty()))
                return finalResList;
        }
        return null;
    }

    //find a matching trade order to make a trade by criteria of price and quantity
    //returns null if no such order found
    public Order findMatch(Order o) {
        if (list.isEmpty())
            return null;
        int currPrice;
        List<Order> priceList;

        try {
            if (o.isSelling()) {
                currPrice = list.lastKey();
                priceList = getTradeOrderListByPrice(currPrice);

                if (o instanceof MKTOrder) {
                    while (priceList == null) {
                        currPrice = list.lowerKey(currPrice);
                        priceList = getTradeOrderListByPrice(currPrice);
                    }
                } else {
                    while (priceList == null && currPrice <= o.getPrice()) {
                        currPrice = list.lowerKey(currPrice);
                        priceList = getTradeOrderListByPrice(currPrice);
                    }
                    if (currPrice < o.getPrice())
                        return null;
                }
                if (priceList == null)
                    return null;
            } else {  //its a buy order
                currPrice = list.firstKey();
                priceList = getTradeOrderListByPrice(currPrice);
                if (o instanceof MKTOrder) {
                    while (priceList == null) {
                        currPrice = list.higherKey(currPrice);
                        priceList = getTradeOrderListByPrice(currPrice);
                    }
                } else {
                    while (priceList == null && currPrice >= o.getPrice()) {
                        currPrice = list.higherKey(currPrice);
                        priceList = getTradeOrderListByPrice(currPrice);
                    }
                    if (currPrice > o.getPrice())
                        return null;
                }

                if (priceList == null)
                    return null;
            }
            return priceList.get(0);
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public String toString(){
        String s= "";
        int sum = 0;

        for (Map.Entry<Integer,List<Order>> pair: list.entrySet()){
            for (Order o : pair.getValue()){
                s = s.concat(o.toString()+"\n");
                sum += (o.getPrice()*o.getQuantity());
            }
        }
        if (sum > 0) {
            s = s.concat("Total Predicted Cycle: " + Order.intToString(sum) + "\n");
        }

        else { //(s.equals("")) {
            return "There are no Trade Orders in the system";
        }
        return s;
    }

    public List<TradeLog> getLogList() {
        List<TradeLog> res= new ArrayList<>();
        getAllTradeOrdersAsList().forEach((order)->{
            res.add(new TradeLog(order));
        });
        return res;
    }
}
