package time;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHandler implements HttpHandler{

    void sendJson(int statusCode, Object object, HttpExchange t) throws IOException {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String str = ow.writeValueAsString(object);
            t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            t.getResponseHeaders().set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            t.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
            t.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
            t.getResponseHeaders().set("Content-Type", "application/json");
            t.sendResponseHeaders(statusCode, str.length());
            OutputStream os = t.getResponseBody();
            os.write(str.getBytes(StandardCharsets.UTF_8));
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
            }
            return "error";
    }

}
