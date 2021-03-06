package webpage.handlers;

import webpage.responseFormats.SearchResponse;
import webpage.responseFormats.SoftGameResponse;
import webpage.responseFormats.UserResponse;
import webpage.util.HandlerType;

import java.util.List;
import java.util.Optional;

import static spark.Spark.get;
import static spark.Spark.path;
import static webpage.entity.Games.searchStringInGames;
import static webpage.entity.Users.getIdByToken;
import static webpage.entity.Users.searchStringInUsers;
import static webpage.util.Parser.toJson;

public class SearchHandler extends AbstractHandler{

    public void handle(){
        path("/search", () -> {
           get("/:searchString","application/json", (req, res) -> {
               String searchString = req.params(":searchString");
               String token = req.headers("token");
               if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

               Long userId = getIdByToken(token);
               Optional<List<SoftGameResponse>> games = searchStringInGames(searchString.toUpperCase(), userId);
               Optional<List<UserResponse>> users = searchStringInUsers(searchString.toUpperCase(), userId);
               if (games.isEmpty() || users.isEmpty()) return returnJson(res, 500, "Something went wrong");
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
