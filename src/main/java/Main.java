import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/hello/:name", (request, response) -> response.raw());
        get("/hello/:name", (request, response) -> response.status());
    }
}