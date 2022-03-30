package webpage.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import webpage.entity.GameList;
import webpage.entity.Status;
import webpage.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyHandler extends AbstractHandler{
    @Override
    public void handle(HttpExchange t) throws IOException {
        GameList gameList = new GameList();
        gameList.addGame(1233, 9, Status.COMPLETED);
        gameList.addGame(48794,2,Status.DROPPED);

        User juan = new User();
        juan.setNickName("juan");
        User user = new User();
        List list = new ArrayList<User>();
        list.add(juan);
        user.setFollowers(list);
        user.setPassword("12345678");
        String str = user.getPassword();
        user.setNickName("pedro");
        sendJson(200, user, t);

    }


}
