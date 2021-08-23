package data.user;

import generated.RseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserList {

    private Map<String, User> userList;
    private int userListVersion;

    public UserList() {
        userList = new TreeMap<>();
        userListVersion = 0;
    }

    public void addUser (RseUser u){
        User user = new User(u);
        addUser(user);

    }

    public void addUser (User u){
        userList.put(u.getName(),u);
        userListVersion++;
    }

    public void clear(){
        userList.clear();
        userListVersion++;
    }

    public final List<User> getUserList() {
        return new ArrayList<>(userList.values());
    }

    public User deleteUser(String username){
        User deletedUser = userList.remove(username);
        if (deletedUser == null)
            userListVersion++;
        return deletedUser;

    }

    public boolean isUserExist(String username){
        return userList.containsKey(username);
    }

    public User getUser(String username) {
        return userList.get(username);
    }

    public int getListVersion() {
        return userListVersion;
    }
}
