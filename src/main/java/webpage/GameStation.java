package webpage;

import webpage.handlers.*;
import webpage.util.Handler;
import webpage.util.HandlerProvider;
import webpage.util.HandlerProviderImpl;
import webpage.util.HandlerType;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.*;

public class GameStation {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GameStation");
        staticFiles.location("/public");
        port(8443);
        enableCORS();
        HandlerProvider handlerProvider = new HandlerProviderImpl(emf);
        final Iterable<Handler> handlers = handlerProvider.getAllHandlers();
        for (final Handler handler : handlers) handler.handle();
        new MyHandler(emf).handle();
    }

    private static void enableCORS() {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "Origin, X-Requested-With, Content-Type, Accept, Authorization, token, imgType, gameId");
            response.header("Access-Control-Allow-Headers", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
            response.type("application/json");
        });
    }
}