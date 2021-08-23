package home;

import engine.Engine;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@WebServlet(name="XMLLoaderServlet", urlPatterns = "/pages/home/loadXML")
@MultipartConfig
public class XMLLoaderServlet extends HttpServlet {

    private static final String HOME_URL = "home.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(HOME_URL);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!ServletUtils.validate(req)){
            resp.sendRedirect(ServletUtils.LOGIN_URL);
        }

        Engine engine = ServletUtils.getEngine(getServletContext());

        Collection<Part> parts = req.getParts();

        for (Part part : parts){
            try {
                synchronized (this) {
                    List<String> alerts = engine.loadXML(part.getInputStream(), ServletUtils.getUserNameFromSession(req));

                    //if loading failed, code execution won't reach after this line
                    //and will go to one of the catch blocks
                    if (alerts != null) {
                        resp.getWriter().write(ServletUtils.stringifyAlerts(alerts));
                    }
                    resp.setStatus(201);
                }
                return;
            }
            catch (IllegalArgumentException e){
                resp.setStatus(409);
                resp.getWriter().write("XML data Loading failed!\n" + e.getMessage());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
