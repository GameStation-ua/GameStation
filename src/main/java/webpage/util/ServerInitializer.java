package webpage.util;

import webpage.handlers.NotificationHandler;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.*;

public class ServerInitializer {

    public static void initializeHandlers(){
        webSocket("/notifications", NotificationHandler.class);
        HandlerProvider handlerProvider = new HandlerProviderImpl();
        final Iterable<Handler> handlers = handlerProvider.getAllHandlers();
        for (final Handler handler : handlers) handler.handle();
    }

    public static void generateEMF(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GameStation");
        EntityManagers.setFactory(emf);
    }

    public static void enableCORS() {

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
