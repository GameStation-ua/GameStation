package webpage.handlers;

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
import static webpage.util.Parser.toJson;

public class SearchHandler extends AbstractHandler{

    public void handle(){
        path("/search", () -> {
           get("/:searchString", (req, res) -> {
               String searchString = req.params(":searchString");
               String token = req.headers("token");
               if (!verifyJWT(token)) {
                   return returnMessage(res, 401, "Not logged in");
               }

               Optional<List<GameResponse>> games = searchStringInGames(searchString.toUpperCase());
               Optional<List<UserResponse>> users = searchStringInUsers(searchString.toUpperCase());
               if (games.isEmpty() || users.isEmpty()){
                   return returnMessage(res, 500, "Something went wrong");
               }
               res.status(200);
               return toJson(new SearchResponse(games.get(),users.get()));
           });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.SEARCH;
    }
}
