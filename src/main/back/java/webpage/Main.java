package webpage;

import com.sun.net.httpserver.HttpServer;
import webpage.handlers.HomeHandler;
import webpage.handlers.LogInHandler;
import webpage.handlers.MyHandler;
import webpage.handlers.RegisterHandler;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8443), 0);

        server.createContext("/main", new MyHandler()); // creates path
        server.createContext("/auth/login", new LogInHandler());
        server.createContext("/home", new HomeHandler());
        server.createContext("/register", new RegisterHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}