package home;


import com.google.gson.Gson;
import data.stock.Stock;
import data.stock.StockDTO;
import engine.Engine;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StockListServlet", urlPatterns = "/pages/home/stockList")
public class StockListServlet extends HttpServlet {

    private static int VERSION = 0;

    public void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()){
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext());
            List<StockDTO> stockDTOList = getStockListDTO(engine.getStockList());
            String jsonResponse = gson.toJson(stockDTOList);
            out.print(jsonResponse);
            out.flush();
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

    private List<StockDTO> getStockListDTO(List<Stock> list){
        List <StockDTO> listDTO = new ArrayList<>();
        list.forEach((stock)->{ listDTO.add(new StockDTO(stock)); });
        return listDTO;
    }
}
