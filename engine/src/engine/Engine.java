package engine;

import data.stock.Stock;
import data.tradeOrder.order.Order;
import data.tradeOrder.order.TradeDTO;
import exception.InvalidStockSymbol;

import java.io.FileNotFoundException;
import java.util.List;

public interface Engine {
    public boolean isDataLoaded();
    public List<Stock> getStockList ();
    public void loadXML(String filePath) throws FileNotFoundException;
    public List<String> getStocksInfo();
    public String getInDepthStockInfo(String symbol) throws InvalidStockSymbol;
    public Stock getStock(String symbol) throws InvalidStockSymbol;
    public TradeDTO addOrder(Order o);
    public String getStockBuyersListInfo(String symbol) throws InvalidStockSymbol;
    public String getStockSellersListInfo(String symbol) throws InvalidStockSymbol;




}
