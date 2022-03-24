import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHandler {

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
}
