package webpage;

import com.sun.net.httpserver.HttpServer;
import spark.Spark;
import webpage.handlers.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.InetSocketAddress;
import static webpage.util.MyValues.*;

import static spark.Spark.port;

public class Main {

    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GameStation");
        port(8443);
        new RegisterHandler(emf).handle(emf, register);
    }
}