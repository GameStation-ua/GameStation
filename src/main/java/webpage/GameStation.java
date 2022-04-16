package webpage;

import webpage.handlers.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.*;
import static webpage.util.Paths.*;

public class GameStation {

    public static void main(String[] args) {
        enableCors();
        staticFiles.location("/public");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GameStation");
        port(8443);
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        new RegisterHandler(emf).handle(register);
        new LogInHandler(emf).handle(logIn);
        new TagsHandler(emf).handle(tags);
        new HomeHandler(emf).handle(home);
        new UploadHandler(emf).handle(upload);
        new MyHandler(emf).handle("/main");
    }

    private static void enableCors() {
        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });
    }
}