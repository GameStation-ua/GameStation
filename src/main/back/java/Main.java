import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8443), 0);

        server.createContext("/main", new MyHandler()); // creates path
        server.createContext("/auth/login", new MyHandler());

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}