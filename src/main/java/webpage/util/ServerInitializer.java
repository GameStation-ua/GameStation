package webpage.util;

import webpage.handlers.NotificationHandler;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.*;

public class ServerInitializer {

    public final static String ImagesPath = System.getenv("SystemDrive") + "/ImagesGameStation";

    public static void createPaths() throws IOException {
        Files.createDirectories(Paths.get(ImagesPath + "/game_requests"));
        Files.createDirectories(Paths.get(ImagesPath + "/game_updates"));
        Files.createDirectories(Paths.get(ImagesPath + "/games"));
        Files.createDirectories(Paths.get(ImagesPath + "/profile_pictures"));
    }

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
        });
    }
}
