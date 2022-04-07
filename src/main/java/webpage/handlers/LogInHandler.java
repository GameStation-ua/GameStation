package webpage.handlers;

import com.sun.net.httpserver.HttpExchange;
import webpage.entity.User;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class LogInHandler extends AbstractHandler{

    public LogInHandler(EntityManagerFactory emf) {
        super(emf);
    }


    public void handle(HttpExchange t) throws IOException {
        User user = new User();
        user.setUserName("pedro");
        user.setPassword("wasd");

    }
}


