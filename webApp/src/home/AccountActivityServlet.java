package home;

import com.google.gson.Gson;
import data.user.User;
import data.util.UserActivityLog;
import engine.Engine;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AccountActivityServlet", urlPatterns = "/pages/home/account/log")
public class AccountActivityServlet extends HttpServlet {

    private final static String LOG_VERSION_PARAM = "logVersion";

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (!ServletUtils.validate(req)){
            resp.sendRedirect(ServletUtils.LOGIN_URL);
        }
        resp.setContentType("application/json");

        Engine engine = ServletUtils.getEngine(getServletContext());
        String username = ServletUtils.getUserNameFromSession(req);
        User user = engine.getUsers().getUser(username);


        int clientLogVersion = ServletUtils.getIntParameter(req,LOG_VERSION_PARAM);
        if (clientLogVersion == ServletUtils.INT_PARAMETER_ERROR){
            return;
        }

        int logManagerVersion = 0;
        List<UserActivityLog> userActivityLogs;
        synchronized (getServletContext()){
            logManagerVersion = user.getActivityLogVersion();
            userActivityLogs = user.getUserLogs(clientLogVersion);
        }

        LogsAndVersion logsAndVersion = new LogsAndVersion(userActivityLogs,logManagerVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(logsAndVersion);

        System.out.println("user "+username+" logs: ");
        userActivityLogs.forEach(log->System.out.println(log.getLog()));

        try(PrintWriter out = resp.getWriter()){
            out.print(jsonResponse);
            out.flush();
        }

    }

    private static class LogsAndVersion{
        private List<UserActivityLog> entries;
        final private int version;

        public LogsAndVersion(List<UserActivityLog> entries, int version) {
            this.entries = entries;
            this.version = version;
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
