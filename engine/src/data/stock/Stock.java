package data.stock;


import data.tradeOrder.list.CompletedTradesList;
import data.tradeOrder.list.OrderList;
import data.tradeOrder.order.CompletedOrder;
import data.tradeOrder.order.Order;


public class Stock implements Comparable<Stock>{

    public final String symbol;
    private int price;
    private final String company;


    private OrderList buyersList;
    private OrderList sellersList;
    private CompletedTradesList completedList;


    public Stock(String symbol, int price, String company)
    {
        this.symbol = symbol.toUpperCase();
        this.price = price;
        this.company = company;

        buyersList = new OrderList();
        sellersList = new OrderList();
        completedList = new CompletedTradesList();
    }

    public String getSymbol()
    {
        return symbol;
    }
    public String getCompany()
    {
        return company;
    }
    public int getPrice()
    {
        return price;
    }
    public int getTotalTrades()
    {
            return completedList.getLength();
    }
    public int getCycle()
    {
        return completedList.getCycle();
    }

    public OrderList getBuyersList(){
        return buyersList;
    }

    public OrderList getSellersList() {
        return sellersList;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public CompletedTradesList getCompletedList() {
        return completedList;
    }

    public void addCompletedTrade(Stock s, int quantity, int price) {
        completedList.add(s,quantity,price);
    }

    public void addCompletedTrade(CompletedOrder o){
        completedList.add(o);
    }

    public String getDetailedInfo()
    {
        return completedList.toString();
    }

    public String buyersListToString()
    {
        return buyersList.toString();
    }

    public String sellersListToString()
    {
        return sellersList.toString();
    }

    @Override
    public int compareTo(Stock o) {
        return symbol.compareTo(o.symbol);
    }

    public boolean equals(Stock o) {
        return super.equals(o) && symbol.equals(o.symbol);
    }

    @Override
    public String toString()
    {
        int trades = getTotalTrades();
        int cycle = getCycle();
       return ("| SYMBOL: "+symbol+"| Corp. name: "+company+
                "| Current price: "+ Order.intToString(price)+"| Trades done: "+Order.intToString(trades)+
                "| Business Cycle: "+Order.intToString(cycle)+"|");

    }



}


