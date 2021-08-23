package data;

import com.google.gson.Gson;
import data.stock.Stock;
import data.tradeOrder.list.CompletedTradesList;
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
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "CompletedTradesDataServlet", urlPatterns = "/pages/data/completed")
public class CompletedTradesDataServlet extends HttpServlet {

    private final static String LOG_VERSION_PARAM = "logVersion";


    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

       if (!ServletUtils.validate(req)) {
            resp.sendRedirect(ServletUtils.LOGIN_URL);
       }
        resp.setContentType("application/json");

        Engine engine = ServletUtils.getEngine(getServletContext());
        String symbol = req.getParameter(ServletUtils.SYMBOL_PARAM);



        int clientLogVersion = ServletUtils.getIntParameter(req, LOG_VERSION_PARAM);
        if (clientLogVersion == ServletUtils.INT_PARAMETER_ERROR) {
            return;
        }
        Stock stock=null;
        int logManagerVersion = 0;
        List<TradeLog> completedTradeLogs = null;
        try {
            synchronized (getServletContext()) {
                stock = engine.getStock(symbol);
                CompletedTradesList completedTradesList = stock.getCompletedList();
                completedTradeLogs = completedTradesList.getLogList(clientLogVersion);
                logManagerVersion = completedTradesList.getLogVersion();
            }
        }
        catch (InvalidStockSymbol invalidStockSymbol) {
            invalidStockSymbol.printStackTrace();
        }
        assert completedTradeLogs != null;

        LogsAndVersion logsAndVersion = new LogsAndVersion(
                completedTradeLogs,logManagerVersion, stock.getPrice(), stock.getCycle());

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(logsAndVersion);

        System.out.println("symbol "+symbol+" logs: ");
        completedTradeLogs.forEach(log->System.out.println(log.getLog()));

        try(PrintWriter out = resp.getWriter()){
            out.print(jsonResponse);
            out.flush();
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


    private static class LogsAndVersion{
        private final List<TradeLog> entries;
        final private int version;
        final private int price;
        final private int cycle;

        public LogsAndVersion(List<TradeLog> entries, int version, int price, int cycle) {
            this.entries = entries;
            this.version = version;
            this.price = price;
            this.cycle = cycle;
        }
    }

}
