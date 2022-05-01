package webpage.handlers;

import com.google.gson.Gson;
import webpage.model.AvailableTag;
import webpage.model.Game;
import webpage.model.User;
import webpage.requestFormats.AvailableTagsRequest;
import webpage.requestFormats.UserTagsRequest;
import webpage.responseFormats.AvailableTagsResponse;
import webpage.responseFormats.GameResponse;
import webpage.responseFormats.SearchTagResponse;
import webpage.responseFormats.UserTagsResponse;
import webpage.util.HandlerType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Games.*;
import static webpage.entity.Persister.merge;
import static webpage.entity.Tags.*;
import static webpage.entity.Users.*;

public class TagsHandler extends AbstractHandler {

    public void handle() {
        path("/tags", () -> {
            path("/available_tags", () -> {
                get("", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                    Optional<List<AvailableTag>> availableTags = getAvailableTags();
                    if (availableTags.isEmpty()) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                    AvailableTagsResponse response = new AvailableTagsResponse(availableTags.get());
                    res.status(200);
                    return new Gson().toJson(response);
                });

                patch("/add", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }

                    boolean isAdmin = getIsAdminByToken(token);
                    if (!isAdmin) {
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
                    }

                    AvailableTagsRequest tagsRequest = new Gson().fromJson(req.body(), AvailableTagsRequest.class);

                    Optional<List<AvailableTag>> availableTags = getAvailableTags();
                    if (availableTags.isEmpty()) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                    try {
                        addAvailableTags(tagsRequest.getTags(), availableTags.get());
                    } catch (Throwable r) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong, try again.\"}";
                    }
                    res.status(200);
                    return "{\"message\":\"Tags successfully added.\"}";
                });

                delete("/delete", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }

                    boolean isAdmin = getIsAdminByToken(token);
                    if (!isAdmin) {
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
                    }

                    AvailableTagsRequest tagsRequest = new Gson().fromJson(req.body(), AvailableTagsRequest.class);

                    Optional<List<AvailableTag>> availableTags = getAvailableTags();
                    if (availableTags.isEmpty()) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                    try {
                        removeAvailableTags(tagsRequest.getTags(), availableTags.get());
                    } catch (Throwable r) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong, try again.\"}";
                    }
                    res.status(200);
                    return "{\"message\":\"Tags successfully removed.\"}";
                });
            });

            path("/users", () -> {
                get("", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                    Long userId = getIdByToken(token);

                    Optional<User> user = findUserById(userId);
                    if (user.isEmpty()) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }

                    UserTagsResponse response = new UserTagsResponse(user.get().getLikedTags());
                    res.status(200);
                    return new Gson().toJson(response);
                });

                delete("/delete", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                    UserTagsRequest tagsRequest = new Gson().fromJson(req.body(), UserTagsRequest.class);

                    Long userId = getIdByToken(token);

                    Optional<User> user = findUserById(userId);
                    if (user.isEmpty()) {
                        res.status(400);
                        return "{\"message\":\"User not found\"}";
                    }

                    removeTagsFromUser(tagsRequest.getTags(), user.get());
                    try {
                        merge(user.get());
                        res.status(200);
                        return "{\"message\":\"Tags successfully removed\"}";
                    } catch (Throwable e) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                });

                patch("/add", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                    UserTagsRequest tagsRequest = new Gson().fromJson(req.body(), UserTagsRequest.class);

                    Long userId = getIdByToken(token);
                    Optional<User> user = findUserById(userId);

                    if (!tagsExist(tagsRequest.getTags()) || user.isEmpty()) {
                        res.status(400);
                        return "{\"message\":\"Something went wrong.\"}";
                    }

                    addTagsToUser(tagsRequest.getTags(), user.get());

                    try {
                        merge(user.get());
                        res.status(200);
                        return "{\"message\":\"Tags successfully added.\"}";
                    } catch (Throwable e) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                });
            });

            path("/games", () -> {
                patch("/add/:gameId", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                    UserTagsRequest tagsRequest = new Gson().fromJson(req.body(), UserTagsRequest.class);

                    Optional<Game> game = findGameById(Long.valueOf(req.params(":gameId")));

                    if (!tagsExist(tagsRequest.getTags()) || game.isEmpty()) {
                        res.status(400);
                        return "{\"message\":\"Something went wrong.\"}";
                    }

                    Long userId = getIdByToken(token);

                    if (!Objects.equals(game.get().getCreatorId(), userId)) {
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
                    }

                    addTagsToGame(tagsRequest.getTags(), game.get());

                    try {
                        merge(game);
                        res.status(200);
                        return "{\"message\":\"Tags added.\"}";
                    } catch (Throwable e) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                });

                delete("/delete/:gameId", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                    UserTagsRequest tagsRequest = new Gson().fromJson(req.body(), UserTagsRequest.class);

                    Optional<Game> game = findGameById(Long.valueOf(req.params(":gameId")));

                    if (!tagsExist(tagsRequest.getTags()) || game.isEmpty()) {
                        res.status(400);
                        return "{\"message\":\"Something went wrong.\"}";
                    }

                    Long userId = getIdByToken(token);

                    if (!Objects.equals(game.get().getCreatorId(), userId)) {
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
                    }

                    removeTagsFromGame(tagsRequest.getTags(), game.get());

                    try {
                        merge(game);
                        res.status(200);
                        return "{\"message\":\"Tags removed.\"}";
                    } catch (Throwable e) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                });
            });

            get("/search/:searchTag", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                String searchTag = req.params(":searchTag");
                Optional<List<Game>> games = searchGameByTag(searchTag);
                if (games.isEmpty()) {
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
                List<GameResponse> gamesForResponse = getGameResponses(games.get());

                return new Gson().toJson(new SearchTagResponse(gamesForResponse));
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.TAGS;
    }
}
