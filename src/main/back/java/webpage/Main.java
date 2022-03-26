package webpage;

import com.sun.net.httpserver.HttpServer;
import webpage.service.LogInHandler;
import webpage.service.MyHandler;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8443), 0);

        server.createContext("/main", new MyHandler()); // creates path
        server.createContext("/auth/login", new LogInHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}