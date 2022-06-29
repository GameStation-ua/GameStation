package webpage.handlers;

import webpage.model.UserGame;
import webpage.requestFormats.GameListRequest;
import webpage.responseFormats.GameListItem;
import webpage.util.HandlerType;

import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Games.findTitlesByUserGames;
import static webpage.entity.Games.prepareGameListResponse;
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
            get("/:userId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

                Long userId = (long) Integer.parseInt(req.params("userId"));
                Optional<List<UserGame>> gameList = findUserGameByUserId(userId);
                if (gameList.isEmpty()) return returnJson(res, 500, "Something went wrong");

                Optional<List<String>> titles = findTitlesByUserGames(gameList.get());
                if (titles.isEmpty()) return returnJson(res, 500, "Something went wrong");

                List<GameListItem> gameListItems = prepareGameListResponse(gameList.get(), titles.get());

                return toJson(gameListItems);
            });

            patch("/add","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);
                if (userGame.isPresent()) return returnJson(res, 409, "Game already in game list");
                UserGame userGame1 = new UserGame(userId, gameListRequest.getStatus(), gameListRequest.getScore(), gameListRequest.getGameId());
                try {
                    merge(userGame1 );
                    return returnJson(res, 200, "Game added to game list");
                }catch (Throwable e) {
                    return returnJson(res, 500, "Something went wrong, try again");
                }
            });

            patch("/delete","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);
                if (userGame.isEmpty()) return returnJson(res, 400, "Bad request");

                try {
                    remove(userGame.get());
                    return returnJson(res, 200, "Game removed to game list");
                }catch (Throwable e) {
                    return returnJson(res, 500, "Something went wrong, try again");
                }
            });

            patch("/edit","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                GameListRequest gameListRequest = fromJson(req.body(), GameListRequest.class);

                Optional<UserGame> userGame = findUserGameByUserIdAndGameId(gameListRequest.getGameId(), userId);

                if (userGame.isEmpty()) return returnJson(res, 400, "Bad request");
                userGame.get().setScore(gameListRequest.getScore());
                userGame.get().setStatus(gameListRequest.getStatus());
                try {
                    merge(userGame.get());
                    return returnJson(res, 200, "Game edited");
                }catch (Throwable e) {
                    return returnJson(res, 500, "Something went wrong, try again");
                }
            });
        });

    }

    @Override
    public HandlerType getType() {
        return HandlerType.GAMELIST;
    }
}
