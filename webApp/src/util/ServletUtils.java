package util;

import data.stock.Stock;
import engine.Engine;
import engine.RiseEngineV3;
import exception.InvalidStockSymbol;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

public class ServletUtils {


    public static final String USER_LIST_PROPERTY_NAME = "userList";
    public static final String STOCK_LIST_PROPERTY_NAME = "stockList";
    public static final String ENGINE_PROPERTY_NAME = "engine";
    public static final String USERNAME = "username";
    public static final String ROLE = "role";
    public static final int INT_PARAMETER_ERROR = -1;
    public static final String SYMBOL_PARAM = "sym";

    public static final String LOGIN_URL = "/login.html";


    private static final Object engineLock = new Object();
    private static final Object sessionLock = new Object();


    public static Engine getEngine(ServletContext servletContext) {
        synchronized (engineLock) {
            if (servletContext.getAttribute(ENGINE_PROPERTY_NAME) == null) {
                Engine engine = new RiseEngineV3();
                servletContext.setAttribute(ENGINE_PROPERTY_NAME, engine);
            }
        }
        return (Engine) servletContext.getAttribute(ENGINE_PROPERTY_NAME);
    }

    public static Stock getStock(ServletContext servletContext, String symbol) throws InvalidStockSymbol {
        return getEngine(servletContext).getStock(symbol);
    }

    public static String getUserNameFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object sessionAttribute = session != null ? session .getAttribute(USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getUserRoleFromSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object sessionAttribute = session != null ? session .getAttribute(ROLE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }


    public static String stringifyAlerts(Collection<String> alerts) {
        StringBuilder res = new StringBuilder("");
        alerts.forEach((res::append));
        return res.toString();
    }


    public static boolean validate(HttpServletRequest req) {
        Engine engine = getEngine(req.getServletContext());
        String username = getUserNameFromSession(req);
        if (username == null || !(engine.getUsers().isUserExist(username))) {
            req.getSession().invalidate();
            return false;
        }
        return true;
    }

    public static int getIntParameter(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return INT_PARAMETER_ERROR;
    }

    public static int getPosIntParameter(HttpServletRequest req, String key) {
        int res = getIntParameter(req, key);
        if (res < 0)
            return INT_PARAMETER_ERROR;
        return res;
    }
}
