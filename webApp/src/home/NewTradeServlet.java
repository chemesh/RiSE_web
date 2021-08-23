package home;

import data.stock.Stock;
import data.tradeOrder.order.*;
import data.user.User;
import engine.Engine;
import exception.InvalidStockSymbol;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "NewTradeServlet", urlPatterns = "/pages/trade")
public class NewTradeServlet extends HttpServlet {

    private final static String TRADE_TYPE_PARAM = "tradeType";
    private final static String TRADE_ACTION_PARAM = "tradeAction";
    private final static String TRADE_SYMBOL_PARAM = "tradeSymbol";
    private final static String TRADE_AMOUNT_PARAM = "tradeAmount";
    private final static String TRADE_PRICE_PARAM = "tradePrice";

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (!ServletUtils.validate(req)){
            resp.sendRedirect(ServletUtils.LOGIN_URL);
        }

        PrintWriter out = resp.getWriter();
        try {
            TradeDTO tradeDTO = tradeOrderFromRequest(req);
            out.write(tradeDTO.toString());
            out.flush();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(404);
            out.write(e.getMessage());
            out.flush();

        }
    }

    private TradeDTO tradeOrderFromRequest(HttpServletRequest req) throws IllegalArgumentException{
        String type = req.getParameter(TRADE_TYPE_PARAM);
        String action = req.getParameter(TRADE_ACTION_PARAM);
        String symbol = req.getParameter(TRADE_SYMBOL_PARAM);
        int amount = ServletUtils.getPosIntParameter(req,TRADE_AMOUNT_PARAM);
        int price = ServletUtils.getPosIntParameter(req,TRADE_PRICE_PARAM);

        if (type==null || action==null || amount==ServletUtils.INT_PARAMETER_ERROR){
            throw new IllegalArgumentException("error in collecting trade order parameters");
        }
        if (price==ServletUtils.INT_PARAMETER_ERROR && !(type.equalsIgnoreCase("mkt"))){
            throw new IllegalArgumentException("error in collecting trade order parameters");
        }

        Engine engine = ServletUtils.getEngine(getServletContext());
        String username = ServletUtils.getUserNameFromSession(req);

        Stock stock = null;
        User user = null;
        try{
            synchronized (getServletContext()) {
                stock = engine.getStock(symbol);
                user = engine.getUsers().getUser(username);
            }
        } catch (InvalidStockSymbol invalidStockSymbol) {
            invalidStockSymbol.printStackTrace();
        }

        Order order;
        switch (type){
            case ("lmt"):
                order = new LMTOrder(action,stock,amount,price,user);
                break;
            case ("mkt"):
                order = new MKTOrder(action,stock,amount,user);
                break;
            case ("fok"):
                order = new FOKOrder(action,stock,amount,price,user);
                break;
            case ("ioc"):
                order = new IOCOrder(action,stock,amount,price,user);
                break;
            default:
                throw new IllegalArgumentException("bad trade order type: "+type);
        }

        TradeDTO res = null;
        synchronized (getServletContext()){
            res = engine.addOrder(order);
        }
        return res;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
