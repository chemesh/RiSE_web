package login;


import data.user.User;
import engine.Engine;
import util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet (name="loginServlet", urlPatterns = "/pages/login")
public class LoginServlet extends HttpServlet {


    private static final String URL_ON_SUCCESS = "home.html";
    private static final String LOGIN_URL = "login.html";

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        response.setContentType("text/plain;charset=UTF-8");
        Engine engine = ServletUtils.getEngine(getServletContext());
        String userFromSession = ServletUtils.getUserNameFromSession(request); //check if username already logged in

        if (userFromSession == null) {
            String username = request.getParameter(ServletUtils.USERNAME);
            if (username == null || username.isEmpty()) { //no value was entered as username
                response.setStatus(409);
                response.getOutputStream().println(LOGIN_URL);
            }
            else {
                synchronized (this) {
                    if (engine.getUsers().isUserExist(username)) {   //if username exist in a different session
                        String errorMsg = "Username '" + username + "' already exists. Please enter a different username";
                        response.setStatus(401);
                        response.getOutputStream().print(errorMsg);

                    } else {          //the username doesn't exist
                        String role = request.getParameter(ServletUtils.ROLE);
                        engine.getUsers().addUser(new User(username,role));
                        request.getSession().setAttribute(ServletUtils.USERNAME, username);
                        request.getSession().setAttribute(ServletUtils.ROLE, role);

                        //cookies for client-side webpage fast update and manipulation
                        //cookie1 = {"username":"avrum"}
                        //cookie2 = {"role":"trader"}
                        response.addCookie(new Cookie(ServletUtils.USERNAME,username));
                        response.addCookie(new Cookie(ServletUtils.ROLE,role));

                        response.setStatus(200);
                        response.getOutputStream().println(URL_ON_SUCCESS);
                        //getServletContext().getRequestDispatcher(HOME_JSP).forward(request,response);
                    }
                }
            }
        }
        else{
            response.getOutputStream().println(URL_ON_SUCCESS);
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
