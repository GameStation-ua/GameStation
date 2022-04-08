package webpage;

import webpage.handlers.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static spark.Spark.staticFiles;
import static webpage.util.MyValues.*;

import static spark.Spark.port;

public class Main {

    public static void main(String[] args) throws Exception {
        staticFiles.location("/public");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GameStation");
        port(8443);
        new RegisterHandler(emf).handle(register);
//        new MyHandler(emf).handle(emf, "/main");
    }
}