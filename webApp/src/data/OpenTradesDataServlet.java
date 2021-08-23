package data;

import com.google.gson.Gson;
import data.stock.Stock;
import data.tradeOrder.list.OrderList;
import data.util.TradeLog;
import engine.Engine;
import exception.InvalidStockSymbol;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "OpenTradesDataServlet", urlPatterns = "/pages/data/admin")
public class OpenTradesDataServlet extends HttpServlet {


    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (!ServletUtils.validate(req)) {
            resp.sendRedirect(ServletUtils.LOGIN_URL);
        }
        resp.setContentType("application/json");

        Engine engine = ServletUtils.getEngine(getServletContext());
        String symbol = req.getParameter(ServletUtils.SYMBOL_PARAM);


        Stock stock=null;
        List<TradeLog> sellTradeLogs = null;
        List<TradeLog> buyTradeLogs = null;

        try {
            synchronized (getServletContext()) {
                stock = engine.getStock(symbol);
                OrderList buyerList = stock.getBuyersList();
                OrderList sellerList = stock.getSellersList();
                sellTradeLogs = sellerList.getLogList();
                buyTradeLogs = buyerList.getLogList();
            }

            Gson gson  = new Gson();
            TradeListsLogs tll = new TradeListsLogs(sellTradeLogs,buyTradeLogs);
            String jsonResponse = gson.toJson(tll);

            resp.getWriter().write(jsonResponse);
            resp.getWriter().flush();

        }
        catch (InvalidStockSymbol invalidStockSymbol) {
            invalidStockSymbol.printStackTrace();
            resp.setStatus(404);
        }
    }

    private static class TradeListsLogs{
        private final List<TradeLog> sellEntries;
        private final List<TradeLog> buyEntries;

        private TradeListsLogs(List<TradeLog> sellEntries, List<TradeLog> buyEntries) {
            this.sellEntries = sellEntries;
            this.buyEntries = buyEntries;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

}
