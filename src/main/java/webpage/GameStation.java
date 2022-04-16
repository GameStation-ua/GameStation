package webpage;

import webpage.handlers.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.*;
import static webpage.util.Paths.*;

public class GameStation {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GameStation");
        staticFiles.location("/public");
        port(8443);
        enableCORS("*","Origin, X-Requested-With, Content-Type, Accept, Authorization, token, imgType, gameId","GET,POST,PUT,DELETE,OPTIONS,HEAD");
        new RegisterHandler(emf).handle(register);
        new LogInHandler(emf).handle(logIn);
        new TagsHandler(emf).handle(tags);
        new HomeHandler(emf).handle(home);
        new UploadHandler(emf).handle(upload);
        new MyHandler(emf).handle("/main");
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {

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
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            response.type("application/json");
        });
    }
}