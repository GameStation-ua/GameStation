package webpage.handlers;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static spark.Spark.before;
import static spark.Spark.options;

public abstract class AbstractHandler{
        EntityManagerFactory emf;

        public AbstractHandler(EntityManagerFactory emf) {
                this.emf = emf;
        }
        public void enableCORS() {

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
                        response.header("Access-Control-Allow-Origin", "*");
                        response.header("Access-Control-Request-Method", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
                        response.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
                        // Note: this may or may not be necessary in your particular application
                        response.type("application/json");
                });
        }

        void sendJson(int statusCode, Object object, HttpExchange t) throws IOException {
            Gson gson = new Gson();
            String s = gson.toJson(object);
            t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            t.getResponseHeaders().set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            t.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
            t.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
            t.getResponseHeaders().set("Content-Type", "application/json");
            t.sendResponseHeaders(statusCode, s.length());
            OutputStream os = t.getResponseBody();
            os.write(s.getBytes(StandardCharsets.UTF_8));
            os.close();
    }

    String receiveJson(HttpExchange t){
            try {
                    Headers requestHeaders = t.getRequestHeaders();
                    int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
                    InputStream is = t.getRequestBody();
                    byte[] data = new byte[contentLength];
                    is.read(data);
                    t.sendResponseHeaders(HttpURLConnection.HTTP_OK, contentLength);
                    OutputStream os = t.getResponseBody();
                    String json = new String(data);
                    os.write(data);
                    t.close();
                    return json;
            } catch (NumberFormatException | IOException e) {
                    return "error";
            }
    }

}
