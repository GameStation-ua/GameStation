package time;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LogInHandler extends AbstractHandler{

    @Override
    public void handle(HttpExchange t) throws IOException {
        User user = new User();
        user.setUserName("pedro");
        user.setPassword("wasd");

    }
}


