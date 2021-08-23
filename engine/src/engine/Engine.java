package engine;

import data.stock.Stock;
import data.stock.StockList;
import data.tradeOrder.order.Order;
import data.tradeOrder.order.TradeDTO;
import data.user.User;
import data.user.UserList;
import exception.InvalidStockSymbol;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface Engine {
    public boolean isDataLoaded();
    public boolean isLastXmlGood ();
    public StockList getStocks();
    public List<Stock> getStockList ();
    public  Map<String,Stock> getStockMap();
    public UserList getUsers();
    public List<User> getUserList();

    public List<String> loadXML(InputStream inputStream, String username);
    public void loadXML(String filePath) throws FileNotFoundException;
    public List<String> getStocksInfo();
    public String getInDepthStockInfo(String symbol) throws InvalidStockSymbol;
    public Stock getStock(String symbol) throws InvalidStockSymbol;
    public TradeDTO addOrder(Order o);
    public String getStockBuyersListInfo(String symbol) throws InvalidStockSymbol;
    public String getStockSellersListInfo(String symbol) throws InvalidStockSymbol;



}
