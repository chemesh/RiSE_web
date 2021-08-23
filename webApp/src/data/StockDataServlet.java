package data;

import data.stock.StockList;
import data.user.User;
import engine.Engine;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StockDataServlet", urlPatterns = "/pages/data")
public class StockDataServlet extends HttpServlet {


    private static final String STOCK_URL = "/pages/data/live.jsp";

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {


        if (!ServletUtils.validate(req)){
            resp.sendRedirect(ServletUtils.LOGIN_URL);
        }

        Engine engine = ServletUtils.getEngine(getServletContext());
        String username = ServletUtils.getUserNameFromSession(req);
        User user = null;
        synchronized (getServletContext()) {
             user = engine.getUsers().getUser(username);
        }

        String symbolFromRequest = req.getParameter(ServletUtils.SYMBOL_PARAM);
        if (symbolFromRequest == null){
            resp.setStatus(401);
            resp.getWriter().print("couldnt fetch symbol from request");
            return;
        }

        StockList stockList;
        synchronized (getServletContext()){
            stockList = engine.getStocks();
        }

        if (stockList.isExist(symbolFromRequest)){
            getServletContext().getRequestDispatcher(STOCK_URL).forward(req,resp);
        }
        else{
            resp.setStatus(402);
            resp.getWriter().print("symbol "+symbolFromRequest+ "couldnt be found in enigne");
        }

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
