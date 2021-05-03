package data.stock;

import exception.InvalidStockSymbol;
import generated.RseStock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StockList {
    // list containing all the stock prototypes in the system
    private Map<String, Stock> stockList;

    public StockList()
    {
        stockList = new TreeMap<>();
    }

    public void addStock(RseStock s)
    {
        addStock(s.getRseSymbol(),s.getRseCompanyName(),s.getRsePrice());
    }

    public void addStock(String sym, String company, int price)
    {
        stockList.put(sym.toUpperCase(),new Stock(sym.toUpperCase(),price,company));
    }
    public Stock getStock(String symbol) throws InvalidStockSymbol
    {
      Stock s = stockList.get(symbol.toUpperCase());
      if (s==null){
        throw new InvalidStockSymbol(symbol);
      }
      return s;
    }
    public String getDetailedStockInfo(String symbol) throws InvalidStockSymbol
    {
        return getStock(symbol).getDetailedInfo();
    }
    public boolean exist(String symbol)
    {
        return stockList.containsKey(symbol.toUpperCase());
    }

    public List<String> ToStringList()
    {
        List<String> strings = new ArrayList<>();

        for (Map.Entry<String,Stock> entry : stockList.entrySet()){
            strings.add(entry.getValue().toString());
        }
        return strings;
    }

    public String getStockBuyersListInfo(String symbol) throws InvalidStockSymbol
    {
        return getStock(symbol).buyersListToString();
    }

    public String getStockSellersListInfo(String symbol) throws InvalidStockSymbol
    {
        return  getStock(symbol).sellersListToString();
    }

    //returns a new ArrayList<Stock>
    public final List<Stock> getStockList ()
    {
        return new ArrayList<Stock>(stockList.values());
    }

    public void clear(){
        stockList.clear();
    }

}
