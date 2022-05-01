package webpage.handlers;

import com.google.gson.Gson;
import webpage.model.Game;
import webpage.model.User;
import webpage.model.UserGame;
import webpage.responseFormats.HardGameForResponse;
import webpage.responseFormats.SoftGameResponse;
import webpage.responseFormats.TagResponse;
import webpage.responseFormats.UserResponse;
import webpage.util.HandlerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Games.findGameById;
import static webpage.entity.Games.findUserGamesbyGameId;
import static webpage.entity.Users.*;
import static webpage.responseFormats.SoftGameResponse.createSoftGameResponseList;
import static webpage.responseFormats.TagResponse.createTagResponseList;

public class ABMGamesHandler extends AbstractHandler{

    @Override
    public void handle() {
// todo implementar ABM juegos
        path("/game", () -> {
            get("/createdgames", (req, res) ->{
                String token = req.headers("token");

                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                    Long userId = getIdByToken(token);

                    Optional<List<Game>> games = findCreatedGamesbyUserId(userId);
                    if (games.isEmpty()){
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                    List<SoftGameResponse> softGameResponseList = createSoftGameResponseList(games.get());


                    return new Gson().toJson(softGameResponseList);
            });

            get("/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                    Long gameId = Long.valueOf(req.params(":gameId"));

                    Optional<Game> game = findGameById(gameId);
                    Optional<List<UserGame>> userGames = findUserGamesbyGameId(gameId);
                if (game.isEmpty() || userGames.isEmpty()){
                    res.status(400);
                    return "{\"message\":\"Something went wrong.\"}";
                }
                float meanScore = getMeanScore(userGames.get());
                Optional<User> creator = findUserById(game.get().getCreatorId());
                if (creator.isEmpty()){
                    res.status(400);
                    return "{\"message\":\"Something went wrong.\"}";
                }
                UserResponse userResponse = new UserResponse(creator.get());
                List<TagResponse> tagsForResponse = createTagResponseList(new ArrayList<>(game.get().getTags()));

                HardGameForResponse gameForResponse = new HardGameForResponse(gameId, meanScore, game.get().getFollowers().size(), game.get().getTitle(), game.get().getDescription(), game.get().getImgsInCarousel(), game.get().getWiki(), userResponse, tagsForResponse, game.get().getGameUpdates());
                res.status(200);
                return new Gson().toJson(gameForResponse);
            });

            post("/create", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                // todo
                return "";
            });
        });
    }

    private float getMeanScore(List<UserGame> userGames) {
        float meanScore = 0;
        if (!userGames.isEmpty()) {
            for (UserGame userGame : userGames) {
                meanScore += userGame.getScore();
            }
            meanScore = meanScore / userGames.size();
        }else {
            meanScore = -1;
        }
        return meanScore;
    }

    @Override
    public HandlerType getType() {
        return HandlerType.ABM_GAMES;
    }
}
