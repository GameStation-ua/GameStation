package webpage.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;
import jdk.dynalink.linker.LinkerServices;
import webpage.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyHandler extends AbstractHandler{
    @Override
    public void handle(HttpExchange t) throws IOException {
        User user = new User();
        user.setPassword("12345678");
        user.setNickName("pedro");
        user.setId(456789789);
        User user2 = new User();
        user2.setPassword("12345678");
        user2.setNickName("pedro");
        user2.setId(456789789);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        sendJson(200, users, t);
    }


}
