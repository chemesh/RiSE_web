package home;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="UserListServlet", urlPatterns = "/pages/home/userList")
public class UserListServlet extends HttpServlet {

    private static final String USER_LIST_VERSION_KEY = "userListVersion";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
            Engine engine = ServletUtils.getEngine(getServletContext());

//        String username = ServletUtils.getUserNameFromSession(req);
//        if (username == null) {
//            resp.sendRedirect(req.getContextPath()+ "/index.html");
//        }
//
//        int userListVersion = ServletUtils.getIntParameter(req,USER_LIST_VERSION_KEY);
//        if (userListVersion == ServletUtils.VERSION_ERROR){
//            return;
//        }

        //int userManagerVersion =0;
        List<User> systemUsers;
        synchronized (getServletContext()){
        //    userManagerVersion = engine.getUsers().getListVersion();
            systemUsers = engine.getUserList();
        }

        //if the current user list inside the engine hasn't been updated
        //since the last time it was sent to the client,
        //
//        if (userManagerVersion <= userListVersion){
//            systemUsers=null;
//        }


        List<UserData> userListObj = UserData.getListFromUsers(systemUsers);
        //UserDataAndVersion udav = new UserDataAndVersion(userListObj,userManagerVersion);
        Gson gson = new Gson();
        //String jsonResponse = gson.toJson(udav);
        String jsonResponse = gson.toJson(userListObj);

        //logServerMessage("server userList version: "+userManagerVersion+", usersData :"+userListObj.toString());
        logServerMessage("usersData :"+userListObj.toString());
        logServerMessage(jsonResponse);

        try (PrintWriter out = resp.getWriter()){
            out.print(jsonResponse);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void logServerMessage(String message){
        System.out.println(message);
    }


    private static class UserDataAndVersion{
        final private List<UserData> data;
        final private int version;

        public UserDataAndVersion(List<UserData> users, int version){
            this.data = users;
            this.version = version;
        }
    }

    private static class UserData{
        final private String username;
        final private String role;

        public UserData(String username, String role){
            this.username = username;
            this.role = role;
        }

        public static List<UserData> getListFromUsers(List<User> list){
            if (list == null)
                return null;
            List<UserData> dataList = new ArrayList<>();
            list.forEach((user)->{
                dataList.add(new UserData(user.getName(),user.getRole()));
            });
            return dataList;
        }

        @Override
        public String toString(){
            return ("["+username+":"+role+"]");
        }
    }
}

