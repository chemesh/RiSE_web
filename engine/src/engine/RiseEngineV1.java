package engine;

import data.stock.Stock;
import data.stock.StockList;
import data.tradeOrder.list.OrderList;
import data.tradeOrder.order.CompletedOrder;
import data.tradeOrder.order.Order;
import data.tradeOrder.order.TradeDTO;
import exception.InvalidStockSymbol;
import generated.RizpaStockExchangeDescriptor;
import generated.RseStock;
import generated.RseStocks;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiseEngineV1 implements Engine{

    private boolean xmlLoaded;
    private StockList stockList;

    public RiseEngineV1()
    {
        xmlLoaded = false;
        stockList = new StockList();
    }


    @Override
    public boolean isDataLoaded() {
        return xmlLoaded;
    }

    @Override
    public void loadXML(String filePath) throws FileNotFoundException {
        String extension = filePath.substring(filePath.lastIndexOf("."));
        if (!extension.equals(".xml")){
            throw new IllegalArgumentException("File Loaded is not of XML type!\n");
        }

        try{
            InputStream inputStream = new FileInputStream(filePath);
            RizpaStockExchangeDescriptor root = deserializeXML(inputStream);
            validateXMLContent(root.getRseStocks());
            setStockList(root.getRseStocks());
            xmlLoaded = true;

        }
        catch (JAXBException e){
            e.printStackTrace(); //debug
        }
    }

    private RizpaStockExchangeDescriptor deserializeXML(InputStream in) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance("generated");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }
    private void validateXMLContent(RseStocks element) throws IllegalArgumentException
    {
        Map<String ,String> symbolTable = new HashMap<>(); //k=symbol, v=corp
        Map<String ,String> corpTable = new HashMap<>(); //k=corp, v=symbol
        List<RseStock> stockList = element.getRseStock();
        for (RseStock s : stockList)
        {
            //check if symbol already exists in another stock
            if (symbolTable.containsKey(s.getRseSymbol())){
                String secondCompany = symbolTable.get(s.getRseSymbol().toUpperCase());
                throw new IllegalArgumentException("Data mismatch: symbol '" +
                        s.getRseSymbol()+ "' found in "+s.getRseCompanyName()+" stock," +
                        " already exists in "+ secondCompany+";"+s.getRseSymbol()+" stock\n");
            }
            //check if Company name already exists in another stock
            else if (corpTable.containsKey(s.getRseCompanyName().toUpperCase())){
                String secondSym = corpTable.get(s.getRseCompanyName());
                throw new IllegalArgumentException("Data mismatch: company name '" +
                        s.getRseCompanyName()+ "' found in "+s.getRseSymbol()+" stock," +
                        " already exists in "+s.getRseCompanyName()+";"+secondSym+" stock\n");
            }
            //check if the price is a positive int
            else if (s.getRsePrice()<0){
                throw new IllegalArgumentException("Invalid data: " +
                        "Stock "+s.getRseCompanyName()+";"+s.getRseSymbol()+
                        "has illegal price ("+s.getRsePrice()+"). price should be a positive integer\n");
            }
            else {
                symbolTable.put(s.getRseSymbol().toUpperCase(),s.getRseCompanyName().toUpperCase());
                corpTable.put(s.getRseCompanyName().toUpperCase(),s.getRseSymbol().toUpperCase());
            }

        }
    }
    private void setStockList(RseStocks element)
    {
        List<RseStock> loadedStocks = element.getRseStock();
        stockList.clear();
        for (RseStock s : loadedStocks)
        {
            stockList.addStock(s);
        }
    }

    @Override
    public List<String> getStocksInfo()
    {
        return stockList.ToStringList();
    }

    @Override
    public String getStockBuyersListInfo(String symbol) throws InvalidStockSymbol {
        return stockList.getStockBuyersListInfo(symbol);
    }

    @Override
    public String getStockSellersListInfo(String symbol) throws InvalidStockSymbol {
        return stockList.getStockSellersListInfo(symbol);
    }

    @Override
    public String getInDepthStockInfo(String symbol) throws InvalidStockSymbol
    {
        String s = stockList.getStock(symbol).toString()+"\n";

        s = s.concat("Additional Trades Information:\n" +
                "--Completed Trades--\n");
        s = s.concat(stockList.getDetailedStockInfo(symbol));
        return s;

    }

    @Override
    public Stock getStock(String symbol) throws InvalidStockSymbol {
        return stockList.getStock(symbol);
    }

    public final List<Stock> getStockList ()
    {
        return stockList.getStockList();
    }

    @Override
    public TradeDTO addOrder(Order o) {

        boolean noMoreTrades = false;
        int tradeQuantity;
        OrderList tradesList;
        CompletedOrder newTrade;
        TradeDTO tradesDTO = new TradeDTO(o.clone());

    ///check kind of trade desired (selling or buying),
    ///and get the list of opposite trade orders
        if (o.isSelling())
            tradesList = o.getStock().getBuyersList();

        else //its a buy order
            tradesList = o.getStock().getSellersList();


    /// main loop. as long as there are possible trades ('findMatch' doesnt return null),
    /// and there are still stocks to trade (o.quantity bigger than 0),
    /// keep searching for possible trades using 'findMatch', and make them
        while(o.getQuantity() > 0 && !noMoreTrades)
        {
            Order oppositeOrder = tradesList.findMatch(o);
            if (oppositeOrder == null)
                noMoreTrades = true;
            else {
                if (o.getQuantity() == oppositeOrder.getQuantity()) {
                    tradeQuantity = o.getQuantity();
                    oppositeOrder.setQuantity(0);
                    o.setQuantity(0);
                }
                else if (o.getQuantity() < oppositeOrder.getQuantity()) {
                    tradeQuantity = o.getQuantity();
                    //update the opposite order stock quantity remained to trade
                    oppositeOrder.setQuantity(oppositeOrder.getQuantity()-o.getQuantity());
                    o.setQuantity(0);
                }
                else // o.quantity > opposite.quantity
                {
                    tradeQuantity = oppositeOrder.getQuantity();
                    o.setQuantity(o.getQuantity()-oppositeOrder.getQuantity());
                    oppositeOrder.setQuantity(0);

                }
                if (oppositeOrder.getQuantity() == 0)
                    //remove the matching order from its list since its completed
                    tradesList.removeOrder(oppositeOrder);

                //write trade to completed trades list and DTO list
                //update latest stock price change
                newTrade = new CompletedOrder(o.getStock(),tradeQuantity,oppositeOrder.getPrice());
                o.getStock().addCompletedTrade(newTrade);
                tradesDTO.add(newTrade);
                o.getStock().setPrice(oppositeOrder.getPrice());
            } //else(oppositeOrder != null)
        } //while

        //if the order hasn't completely made, add it to its respective buy/sell Order list
        if(o.getQuantity() > 0) {
            tradesDTO.setQuantityLeft(o.getQuantity());
            if (o.isSelling())
                o.getStock().getSellersList().addOrder(o);
            else
                o.getStock().getBuyersList().addOrder(o);
        }
        return tradesDTO;
    }
}
