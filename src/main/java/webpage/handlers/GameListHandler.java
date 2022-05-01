package webpage.handlers;

import com.google.gson.Gson;
import webpage.model.UserGame;
import webpage.requestFormats.GameListRequest;
import webpage.util.HandlerType;

import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Persister.merge;
import static webpage.entity.Persister.remove;
import static webpage.entity.UserGames.findUserGameByUserId;
import static webpage.entity.UserGames.findUserGameByUserIdAndGameId;
import static webpage.entity.Users.getIdByToken;

public class GameListHandler extends AbstractHandler{

    @Override
    public void handle() {
        path("/gamelist", () -> {
            get("/:userId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }

                Long userId = (long) Integer.parseInt(req.params("userId"));
                Optional<List<UserGame>> gameList = findUserGameByUserId(userId);
                if (gameList.isEmpty()){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }

                res.status(200);
                return new Gson().toJson(gameList);
            });

            patch("/add", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = new Gson().fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);
                if (userGame.isPresent()){
                    res.status(409);
                    return "{\"message\":\"Game already in game list.\"}";
                }
                UserGame userGame1 = new UserGame(userId, gameListRequest.getStatus(), gameListRequest.getScore(), gameListRequest.getGameId());
                try {
                    merge(userGame1 );
                    res.status(200);
                    return "{\"message\":\"Game added to game list.\"}";
                }catch (Throwable e) {
                    res.status(500);
                    return "{\"message\":\"Something went wrong, try again.\"}";
                }
            });

            patch("/delete", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = new Gson().fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);
                if (userGame.isEmpty()){
                    res.status(400);
                    return  "{\"message\":\"Bad request.\"}";
                }

                try {
                    remove(userGame);
                    res.status(200);
                    return "{\"message\":\"Game removed to game list.\"}";
                }catch (Throwable e) {
                    res.status(500);
                    return "{\"message\":\"Something went wrong, try again.\"}";
                }
            });

            patch("/edit", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = new Gson().fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);

                if (userGame.isEmpty()){
                    res.status(400);
                    return  "{\"message\":\"Bad request.\"}";
                }
                userGame.get().setScore(gameListRequest.getScore());
                userGame.get().setStatus(gameListRequest.getStatus());
                try {
                    merge(userGame.get());
                    res.status(200);
                    return "{\"message\":\"Game edited.\"}";
                }catch (Throwable e) {
                    res.status(500);
                    return "{\"message\":\"Something went wrong, try again.\"}";
                }
            });
        });

    }

    @Override
    public HandlerType getType() {
        return HandlerType.GAMELIST;
    }
}
