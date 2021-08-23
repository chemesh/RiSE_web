package engine;

import data.stock.Stock;
import data.stock.StockList;
import data.tradeOrder.list.OrderList;
import data.tradeOrder.order.*;
import data.user.User;
import data.user.UserList;
import data.util.ValidRseStocksDTO;
import exception.InvalidStockSymbol;
import generated.RizpaStockExchangeDescriptor;
import generated.RseItem;
import generated.RseStock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiseEngineV3 implements Engine{

    private boolean xmlLoaded;
    private boolean lastXmlLaded;
    private StockList stockList;
    private UserList userList;

    public RiseEngineV3(){
        xmlLoaded = false;
        lastXmlLaded = false;
        stockList = new StockList();
        userList = new UserList();
    }

    @Override
    public boolean isDataLoaded() {
        return xmlLoaded;
    }

    @Override
    public final StockList getStocks() {
        return stockList;
    }

    public boolean isLastXmlGood () {
        return lastXmlLaded;
    }

    @Override
    public List<String> loadXML(InputStream inputStream, String username){
        /*
        * return value basically is a collection pf possible alerts that you may or may not
        * want to inform the user about.
        * If there are no alerts at all, the return value will be null
        */
        List<String> alerts = null;
        try{
            //extract and validate content
            RizpaStockExchangeDescriptor root = deserializeXML(inputStream);
            ValidRseStocksDTO validStocks = validateXMLContent(root);

            //load content to the engine
            setStockList(validStocks.getValidRseStocks());
            updateUserItemsOnUpload(root.getRseHoldings().getRseItem(),username);

            //update relevant parties
            alerts = validStocks.getAlerts();
            xmlLoaded = true;
            lastXmlLaded = true;

        }
        catch (JAXBException e){
            e.printStackTrace(); //debug
            lastXmlLaded = false;
        }

        return alerts;
    }

    public void updateUserItemsOnUpload(List<RseItem> rseItems, String username) {
        User user = userList.getUser(username);
        for (RseItem rseItem : rseItems){
            try {
                Stock s = stockList.getStock(rseItem.getSymbol());
                user.updateItemOnUpload(s, rseItem.getQuantity());
            }
            catch (InvalidStockSymbol e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadXML(String filePath) throws FileNotFoundException {
        String extension = filePath.substring(filePath.lastIndexOf("."));
        if (!extension.equals(".xml")){
            throw new IllegalArgumentException("File Loaded is not of XML type!\n");
        }
        InputStream inputStream = new FileInputStream(filePath);
        loadXML(inputStream, null);
    }

    private RizpaStockExchangeDescriptor deserializeXML(InputStream in) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance("generated");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }

    private ValidRseStocksDTO validateXMLContent(RizpaStockExchangeDescriptor element) throws IllegalArgumentException
    {
        Map<String, String> symbolTable = new HashMap<>(); //k=symbol, v=corp
        Map<String, String> corpTable = new HashMap<>(); //k=corp, v=symbol

        //ValidRseStocksDTO members
        List<String> alertList = new ArrayList<>();
        List<RseStock> validStockList = new ArrayList<>();


        List<RseStock> stockList = element.getRseStocks().getRseStock();
        for (RseStock s : stockList) {
            //check if symbol already exists in another stock
            if (symbolTable.containsKey(s.getRseSymbol())) {
                String secondCompany = symbolTable.get(s.getRseSymbol().toUpperCase());
                throw new IllegalArgumentException("Data mismatch: symbol '" +
                        s.getRseSymbol() + "' found in " + s.getRseCompanyName() + " stock," +
                        " already exists in " + secondCompany + ";" + s.getRseSymbol() + " stock\n");
            }
            //check if Company name already exists in another stock
            else if (corpTable.containsKey(s.getRseCompanyName().toUpperCase())) {
                String secondSym = corpTable.get(s.getRseCompanyName());
                throw new IllegalArgumentException("Data mismatch: company name '" +
                        s.getRseCompanyName() + "' found in " + s.getRseSymbol() + " stock," +
                        " already exists in " + s.getRseCompanyName() + ";" + secondSym + " stock\n");
            }
            //check if the price is a positive int
            else if (s.getRsePrice() < 0) {
                throw new IllegalArgumentException("Invalid data: " +
                        "Stock " + s.getRseCompanyName() + ";" + s.getRseSymbol() +
                        "has illegal price (" + s.getRsePrice() + "). price should be a positive integer\n");
            }
            //check if the stock already exist in the engine
            else if(!this.stockList.isEmpty() && this.stockList.isExist(s.getRseSymbol())){
                alertList.add("Stock symbol '"+s.getRseSymbol() +"' already exists in the system. " +
                        "new '" + s.getRseSymbol()+"' stock from the uploaded file won't be registered");
            }
            else { //add the stock to the valid stock list that will be loaded to the engine
                validStockList.add(s);
                symbolTable.put(s.getRseSymbol().toUpperCase(), s.getRseCompanyName().toUpperCase());
                corpTable.put(s.getRseCompanyName().toUpperCase(), s.getRseSymbol().toUpperCase());
            }


        }
        //check RSEHoldings routine
        List<RseItem> itemList = element.getRseHoldings().getRseItem();
        itemList.forEach((rseItem)->{
            if (!((symbolTable.containsKey(rseItem.getSymbol())) || (this.stockList.isExist(rseItem.getSymbol())))){
                throw new IllegalArgumentException("Invalid data: " +
                        "the stock symbol '"+rseItem.getSymbol()+"' for Item "+
                        rseItem.getSymbol()+":"+rseItem.getQuantity()+" does not exist in the file data");
            }
        });
        return new ValidRseStocksDTO(validStockList, alertList);
    }


    private void setStockList(List<RseStock> loadedStocks)
    {
        //stockList.clear();
        for (RseStock s : loadedStocks) {
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

    public final List<Stock> getStockList () {
        return stockList.getStockList();
    }

    public final Map<String,Stock> getStockMap(){
        return stockList.getStockMap();
    }

    @Override
    public UserList getUsers() {
        return userList;
    }

    public final List<User> getUserList(){
        return userList.getUserList();
    }


    @Override
    public TradeDTO addOrder(Order o) {

        boolean noMoreTrades = false;
        int tradeQuantity;
        OrderList tradesList;
        TradeDTO tradesDTO = new TradeDTO(o.clone(),o.getQuantity());
        List<Order> lesserOrders = new ArrayList<>();

    ///check kind of trade desired (selling or buying),
    ///and get the list of opposite trade orders
        if (o.isSelling()) {
            tradesList = o.getStock().getBuyersList();
            //o.updateInitiatorItemAmountOnSelling(o.getStock(),o.getQuantity());
            //controller.updateAcceptorItem(o);
        }
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
                if (o.getQuantity() <= oppositeOrder.getQuantity()) {
                    if (o instanceof FOKOrder) {
                        //if its a FOK order we have to first complete all the previous matched trades
                        completeFOK(o,lesserOrders,tradesList,tradesDTO);
                    }
                    if (o.getQuantity() == oppositeOrder.getQuantity())
                        oppositeOrder.setQuantity(0);

                    else //if (o.getQuantity() < oppositeOrder.getQuantity())
                        oppositeOrder.setQuantity(oppositeOrder.getQuantity()-o.getQuantity());

                    tradesDTO.add(completeTrade(o,oppositeOrder,tradesList,o.getQuantity()));
                    tradesDTO.setQuantityLeft(0);
                    o.setQuantity(0);
//                    if (o.isSelling())
//                        o.updateInitiatorItemAmountOnSelling(o.getStock(), o.getQuantity());
                }
                else // o.quantity > opposite.quantity
                {
                    o.setQuantity(o.getQuantity() - oppositeOrder.getQuantity());
                    if (o instanceof FOKOrder) {
                        oppositeOrder.setPending();
                        lesserOrders.add(oppositeOrder);
                    }
                    else {
                        tradeQuantity = oppositeOrder.getQuantity();
                        oppositeOrder.setQuantity(0);
                        tradesDTO.add(completeTrade(o,oppositeOrder,tradesList,tradeQuantity));
                    }
                }
            } //else(oppositeOrder != null)
        } //while

        //if the order hasn't been completely made, add it to its respective buy/sell Order list
        if(o.getQuantity() > 0){
            if (!(o instanceof FOKOrder)){
                tradesDTO.setQuantityLeft(o.getQuantity());

                if (o.isSelling() && !(o instanceof IOCOrder)) {
                        o.getStock().getSellersList().addOrder(o);
                }
                else if (!(o instanceof IOCOrder)) {
                        o.getStock().getBuyersList().addOrder(o);
                }
            }
            else{
                //its an un-completed FOK Order
                //unpend all orders matched and abort order
                lesserOrders.forEach(Order::unPend);
            }
        }
        return tradesDTO;
    }

    private void completeFOK(Order fok,List<Order> lesserOrders,
                             OrderList tradesList,TradeDTO tradesDTO) {
        int tradeQuantity;
        for(Order oppositeOrder : lesserOrders){
            tradeQuantity = oppositeOrder.getQuantity();
            oppositeOrder.setQuantity(0);
            tradesDTO.add(completeTrade(fok,oppositeOrder,tradesList,tradeQuantity));
        }
        tradesDTO.setQuantityLeft(0);
    }

    private CompletedOrder completeTrade(Order o, Order oppositeOrder,OrderList tradesList, int tradeQuantity){
        CompletedOrder newTrade;
        User acceptor = oppositeOrder.getUser();

        if (oppositeOrder.getQuantity() == 0)
            //remove the matching order from its list since its completed
            tradesList.removeOrder(oppositeOrder);

        //write trade to completed trades list and DTO list
        //update latest stock price change

        newTrade = new CompletedOrder(o.getStock(),tradeQuantity
                ,oppositeOrder.getPrice(),o.getUser(),acceptor,o.isSelling());
//                int acceptorCost= oppositeOrder.getPrice();

        //update users portfolio
        if(o.isSelling()){
            acceptor.updateItem(o.getStock(),tradeQuantity,newTrade);
            o.updateInitiatorItemAmountOnSelling(o.getStock(),tradeQuantity);
            //acceptor.getPortfolio().updateItem(o.getStock(), tradeQuantity);
            //controller.updateAcceptorItem(oppositeOrder);
        }
        else{ //o.isBuying()
            o.getUser().updateItem(o.getStock(),tradeQuantity,newTrade);
            oppositeOrder.updateInitiatorItemAmountOnSelling(o.getStock(),tradeQuantity);
            //acceptor.updateItem(o.getStock(),-tradeQuantity,newTrade);
            //o.getUser().getPortfolio().updateItem(o.getStock(),tradeQuantity);
            //controller.updateAcceptorItem(o);
            // no need to update acceptor because sellers update upon issuing the trade
        }
        o.getStock().addCompletedTrade(newTrade);
        o.getStock().setPrice(oppositeOrder.getPrice());
        return newTrade;
    }
}
