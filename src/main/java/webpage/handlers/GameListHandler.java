package webpage.handlers;

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
import static webpage.util.Parser.fromJson;
import static webpage.util.Parser.toJson;

public class GameListHandler extends AbstractHandler{

    @Override
    public void handle() {
        path("/gamelist", () -> {
            get("/:userId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }

                Long userId = (long) Integer.parseInt(req.params("userId"));
                Optional<List<UserGame>> gameList = findUserGameByUserId(userId);
                if (gameList.isEmpty()){
                    return returnMessage(res, 500, "Something went wrong");
                }

                return toJson(gameList);
            });

            patch("/add", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);
                if (userGame.isPresent()){
                    return returnMessage(res, 409, "Game already in game list");
                }
                UserGame userGame1 = new UserGame(userId, gameListRequest.getStatus(), gameListRequest.getScore(), gameListRequest.getGameId());
                try {
                    merge(userGame1 );
                    return returnMessage(res, 200, "Game added to game list");
                }catch (Throwable e) {
                    return returnMessage(res, 500, "Something went wrong, try again");
                }
            });

            patch("/delete", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);
                if (userGame.isEmpty()){
                    return returnMessage(res, 400, "Bad request");
                }

                try {
                    remove(userGame);
                    return returnMessage(res, 200, "Game removed to game list");
                }catch (Throwable e) {
                    return returnMessage(res, 500, "Something went wrong, try again");
                }
            });

            patch("/edit", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);

                if (userGame.isEmpty()){
                    return returnMessage(res, 400, "Bad request");
                }
                userGame.get().setScore(gameListRequest.getScore());
                userGame.get().setStatus(gameListRequest.getStatus());
                try {
                    merge(userGame.get());
                    return returnMessage(res, 200, "Game edited");
                }catch (Throwable e) {
                    return returnMessage(res, 500, "Something went wrong, try again");
                }
            });
        });

    }

    @Override
    public HandlerType getType() {
        return HandlerType.GAMELIST;
    }
}
