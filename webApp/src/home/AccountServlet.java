package home;

import data.user.User;
import engine.Engine;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="AccountServlet", urlPatterns = "/pages/home/account")
public class AccountServlet extends HttpServlet {

    private final String ACCOUNT_URL = "account.html";


    public void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //getServletContext().getRequestDispatcher(ACCOUNT_URL).forward(req,resp);
        Engine engine = ServletUtils.getEngine(getServletContext());

        String username = ServletUtils.getUserNameFromSession(req);
        if (username == null){
            resp.sendRedirect(ServletUtils.LOGIN_URL);
        }

        User user = engine.getUsers().getUser(username);
        if (user == null) {
            req.getSession().invalidate();
            resp.sendRedirect(ServletUtils.LOGIN_URL);
        }
        resp.sendRedirect(ACCOUNT_URL);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }



    @WebServlet(name = "ChargeCreditServlet", urlPatterns = "/pages/home/account/charge")
    public static class ChargeCreditServlet extends HttpServlet{

        private final String AMOUNT_PARAM = "amount";

        public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

            String username = ServletUtils.getUserNameFromSession(req);
            if (username == null){
                resp.sendRedirect("/login.html");
            }
            int amountToCharge = ServletUtils.getIntParameter(req,AMOUNT_PARAM);
            User user = ServletUtils.getEngine(getServletContext()).getUsers().getUser(username);
            if (user == null){
                resp.setStatus(403);
                try(PrintWriter out = resp.getWriter()){
                    out.write("error: username "+username+" doesnt exist in the system");
                    out.flush();
                    resp.sendRedirect(ServletUtils.LOGIN_URL);
                    return;
                }
            }
            synchronized (getServletContext()){
                user.chargeCredit(amountToCharge);
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


}
