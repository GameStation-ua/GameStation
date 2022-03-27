package webpage.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import webpage.entity.User;
import webpage.exceptions.LoadingExc;
import webpage.util.EntityManagers;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.WriteAbortedException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpHandler extends AbstractHandler{
    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
            User user = new Gson().fromJson(receiveJson(t), User.class);
            if (user.getNickName() == null || user.getPassword() == null || user.getUserName() == null) throw new LoadingExc("Could not load user");
            user.persist();
            HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:8443/home")
                    .openConnection();
        }catch(LoadingExc ex){
            handle(t);
        }
    }
}
