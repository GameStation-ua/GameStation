package webpage;

import webpage.handlers.FollowHandler;
import webpage.handlers.MyHandler;
import webpage.handlers.NotificationHandler;
import webpage.util.EntityManagers;
import webpage.util.Handler;
import webpage.util.HandlerProvider;
import webpage.util.HandlerProviderImpl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.*;

public class GameStation {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GameStation");
        EntityManagers.setFactory(emf);
//        staticFiles.location("/public");
        String projectDir = System.getProperty("user.dir");
        String staticDir = "/src/main/resources/public";
        staticFiles.externalLocation(projectDir + staticDir);
        // use location on real server
        webSocket("/notifications", NotificationHandler.class);
        port(8443);
        enableCORS();
        HandlerProvider handlerProvider = new HandlerProviderImpl();
        final Iterable<Handler> handlers = handlerProvider.getAllHandlers();
        for (final Handler handler : handlers) handler.handle();
        new MyHandler().handle();
    }

    private static void enableCORS() {

        options("/*", (request, response) -> {
            response.status(200);
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.type("application/json");
        });
    }
}