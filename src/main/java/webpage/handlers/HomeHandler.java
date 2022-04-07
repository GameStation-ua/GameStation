package webpage.handlers;

import com.sun.net.httpserver.HttpExchange;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class HomeHandler extends AbstractHandler{
    public HomeHandler(EntityManagerFactory emf) {
        super(emf);
    }


    public void handle(HttpExchange exchange) throws IOException {

    }
}
