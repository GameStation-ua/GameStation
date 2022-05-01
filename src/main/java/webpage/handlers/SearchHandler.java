package webpage.handlers;

import com.google.gson.Gson;
import webpage.responseFormats.GameResponse;
import webpage.responseFormats.SearchResponse;
import webpage.responseFormats.UserResponse;
import webpage.util.HandlerType;

import java.util.List;
import java.util.Optional;

import static spark.Spark.get;
import static spark.Spark.path;
import static webpage.entity.Games.searchStringInGames;
import static webpage.entity.Users.searchStringInUsers;

public class SearchHandler extends AbstractHandler{

    public void handle(){
        path("/search", () -> {
           get("/:searchString", (req, res) -> {
               String searchString = req.params(":searchString");
               String token = req.headers("token");
               if (!verifyJWT(token)) {
                   res.status(401);
                   return "{\"message\":\"Not logged in.\"}";
               }

               Optional<List<GameResponse>> games = searchStringInGames(searchString.toUpperCase());
               Optional<List<UserResponse>> users = searchStringInUsers(searchString.toUpperCase());
               if (games.isEmpty() || users.isEmpty()){
                   res.status(500);
                   return "{\"message\":\"Something went wrong.\"}";
               }

               return new Gson().toJson(new SearchResponse(games.get(),users.get()));
           });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.SEARCH;
    }
}
