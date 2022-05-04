package webpage.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Parser {

    private static final Gson gson = new Gson();

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }
}
