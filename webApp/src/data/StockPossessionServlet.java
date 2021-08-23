package data;

import engine.Engine;
import exception.InvalidStockSymbol;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name ="StockPossessionServlet", urlPatterns = "/pages/data/trader")
public class StockPossessionServlet extends HttpServlet {

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (!ServletUtils.validate(req)) {
            resp.sendRedirect(ServletUtils.LOGIN_URL);
        }

        String username = ServletUtils.getUserNameFromSession(req);
        String symbol = req.getParameter(ServletUtils.SYMBOL_PARAM);
        Engine engine = ServletUtils.getEngine(getServletContext());


        int amount = 0;
        try {
            synchronized (this) {
                amount = engine.getUsers().getUser(username).getPortfolio().getItem(symbol).getQuantity();
            }
            resp.setStatus(200);
            resp.getWriter().write(Integer.toString(amount));
        } catch (InvalidStockSymbol invalidStockSymbol) {
            invalidStockSymbol.printStackTrace();
            resp.setStatus(404);
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
