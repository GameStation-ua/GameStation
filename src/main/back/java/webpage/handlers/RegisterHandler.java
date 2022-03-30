package webpage.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import javafx.scene.control.Alert;
import webpage.entity.User;
import webpage.exceptions.LoadingExc;
import webpage.util.Users;

import java.io.IOException;

public class RegisterHandler extends AbstractHandler{
    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
            String str = receiveJson(t);
            System.out.println(str);
            User user = new Gson().fromJson(str, User.class);
            System.out.println(user.getUserName() + user.getNickName() + user.getPassword());
            if (user.getNickName() == null || user.getPassword() == null || user.getUserName() == null) throw new LoadingExc("Could not load user");
            else Users.persist(user);
        }catch(LoadingExc ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "information incomplete");
            sendJson(409, alert, t);
        }
    }
}
