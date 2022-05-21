package webpage;

import webpage.handlers.NotificationHandler;
import webpage.util.Handler;
import webpage.util.HandlerProvider;
import webpage.util.HandlerProviderImpl;

import static spark.Spark.*;
import static webpage.util.ServerInitializer.enableCORS;
import static webpage.util.ServerInitializer.generateEMF;

public class GameStation {

    public static void main(String[] args) {
        generateEMF();
        //        staticFiles.location("/public");
        String projectDir = System.getProperty("user.dir");
        staticFiles.externalLocation(projectDir + "/src/main/resources/public");
        // use location on real server
        webSocket("/notifications", NotificationHandler.class);
        port(8443);
        enableCORS();
        HandlerProvider handlerProvider = new HandlerProviderImpl();
        final Iterable<Handler> handlers = handlerProvider.getAllHandlers();
        for (final Handler handler : handlers) handler.handle();
    }
}