package webpage;

import webpage.handlers.NotificationHandler;
import webpage.util.Handler;
import webpage.util.HandlerProvider;
import webpage.util.HandlerProviderImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static spark.Spark.*;
import static webpage.util.ServerInitializer.*;

public class GameStation {

    public static void main(String[] args) throws IOException {
        generateEMF();
        //        staticFiles.location("/public");
        createPaths();
        // use location on real server
        webSocket("/notifications", NotificationHandler.class);
        port(8443);
        enableCORS();
        HandlerProvider handlerProvider = new HandlerProviderImpl();
        final Iterable<Handler> handlers = handlerProvider.getAllHandlers();
        for (final Handler handler : handlers) handler.handle();
    }
}