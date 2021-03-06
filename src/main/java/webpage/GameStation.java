package webpage;

import webpage.handlers.NotificationHandler;
import webpage.util.Handler;
import webpage.util.HandlerProvider;
import webpage.util.HandlerProviderImpl;

import java.io.IOException;

import static spark.Spark.port;
import static spark.Spark.webSocket;
import static webpage.api.MailSender.initMailSender;
import static webpage.util.ServerInitializer.*;

public class GameStation {

    public static void main(String[] args) throws IOException {
        generateEMF();
        createPaths();
        webSocket("/notifications", NotificationHandler.class);
        port(8443);
        enableCORS();
        HandlerProvider handlerProvider = new HandlerProviderImpl();
        final Iterable<Handler> handlers = handlerProvider.getAllHandlers();
        for (final Handler handler : handlers) handler.handle();
        initMailSender();
    }
}