import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class MyHandler extends AbstractHandler{
    @Override
    public void handle(HttpExchange t) throws IOException {
        User user = new User();
        user.setPassword("12345678");
        String str = user.getPassword();
        user.setPassword(null);
        user.setNickName("pedro");
        user.setId(456789789);
        sendJson(200, user, t);
        user.setPassword(str);
    }


}
