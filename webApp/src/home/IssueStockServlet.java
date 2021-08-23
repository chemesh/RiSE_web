package home;

import data.stock.Stock;
import engine.Engine;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name = "IssueStockServlet", urlPatterns = "/pages/home/issueStock")
public class IssueStockServlet extends HttpServlet {

    public static final String COMPANY_PARAM = "company";
    public static final String SYMBOL_PARAM = "symbol";
    public static final String QUANTITY_PARAM = "quantity";
    public static final String COMPANY_WORTH_PARAM = "company-worth";



    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!(ServletUtils.validate(req))) {
            throw new IllegalArgumentException("Current User isn't Logged to system");
        }
        else{
            Engine engine = ServletUtils.getEngine(getServletContext());
            String username = ServletUtils.getUserNameFromSession(req);
            try {
                String corpName = req.getParameter(COMPANY_PARAM);
                String symbol = req.getParameter(SYMBOL_PARAM);
                int quantity = getQuantityFromParam(QUANTITY_PARAM,req);
                int price = getPricePerShareFromWorthParam(quantity,req);

                if (engine.getStocks().isExist(symbol))
                    throw new IllegalArgumentException("SYMBOL '"+symbol+"' ALREADY EXIST IN THE SYSTEM");
                else if (engine.getStocks().isCorpExist(corpName))
                    throw new IllegalArgumentException("COMPANY '"+corpName+"' ALREADY EXIST IN THE SYSTEM");
                else {
                    synchronized (getServletContext()) {
                        Stock newStock = new Stock(symbol,price,corpName);
                        engine.getStocks().addStock(newStock);
                        engine.getUsers().getUser(username).getPortfolio().updateItem(newStock,quantity);
                    }
                    resp.setStatus(200);
                }
            }
            catch (IllegalArgumentException e){
                resp.setStatus(403);
                PrintWriter out = resp.getWriter();
                out.print("Error!\n"+e.getMessage());
                out.flush();
            }
        }
    }

    private int getPricePerShareFromWorthParam(int quantity, HttpServletRequest req)
            throws IllegalArgumentException{
        try{
            int worth = getQuantityFromParam(COMPANY_WORTH_PARAM,req);
            return worth/quantity;
        }
        catch (IllegalArgumentException e){
            String msg = "";
            if (e instanceof NumberFormatException){
                msg = e.getMessage();
            }
            else{
                msg = "Illegal Company worth value:\n"+e.getMessage();
            }
            throw new IllegalArgumentException(msg);
        }
    }


    private int getQuantityFromParam(String quantity_param,HttpServletRequest req)
            throws IllegalArgumentException{
        String value = req.getParameter(quantity_param);
        int numValue = Integer.parseInt(value);
        if (numValue <= 0){
            throw new IllegalArgumentException("Illegal Quantity value: "+numValue+".\n Please Submit a positive Value");
        }
        return numValue;
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
