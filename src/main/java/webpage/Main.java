package webpage;

import webpage.handlers.LogInHandler;
import webpage.handlers.MyHandler;
import webpage.handlers.RegisterHandler;
import webpage.handlers.TagsHandler;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static webpage.util.Paths.*;

public class Main {

    public static void main(String[] args) {
        staticFiles.location("/public");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GameStation");
        port(8443);
        new RegisterHandler(emf).handle(register);
        new LogInHandler(emf).handle(logIn);
        new TagsHandler(emf).handle(tags);
        new MyHandler(emf).handle("/main");
    }
}