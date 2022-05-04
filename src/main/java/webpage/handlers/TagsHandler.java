package webpage.handlers;

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
import static webpage.util.Parser.fromJson;
import static webpage.util.Parser.toJson;

public class TagsHandler extends AbstractHandler {

    public void handle() {
        path("/tags", () -> {
            path("/available_tags", () -> {
                get("", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        return returnMessage(res, 401, "Not logged in");
                    }
                    Optional<List<AvailableTag>> availableTags = getAvailableTags();
                    if (availableTags.isEmpty()) {
                        return returnMessage(res, 500, "Something went wrong");
                    }
                    AvailableTagsResponse response = new AvailableTagsResponse(availableTags.get());
                    return returnMessage(res, 200, toJson(response));
                });

                patch("/add", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        return returnMessage(res, 401, "Not logged in");
                    }

                    boolean isAdmin = getIsAdminByToken(token);
                    if (!isAdmin) {
                        return returnMessage(res, 401, "Unauthorized");
                    }

                    AvailableTagsRequest tagsRequest = fromJson(req.body(), AvailableTagsRequest.class);

                    Optional<List<AvailableTag>> availableTags = getAvailableTags();
                    if (availableTags.isEmpty()) {
                        return returnMessage(res, 500, "Something went wrong");
                    }
                    try {
                        addAvailableTags(tagsRequest.getTags(), availableTags.get());
                    } catch (Throwable r) {
                        return returnMessage(res, 500, "Something went wrong, try again");
                    }
                    return returnMessage(res, 200, "Tags successfully added");
                });

                delete("/delete", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        return returnMessage(res, 401, "Not logged in");
                    }

                    boolean isAdmin = getIsAdminByToken(token);
                    if (!isAdmin) {
                        return returnMessage(res, 401, "Unauthorized");
                    }

                    AvailableTagsRequest tagsRequest = fromJson(req.body(), AvailableTagsRequest.class);

                    Optional<List<AvailableTag>> availableTags = getAvailableTags();
                    if (availableTags.isEmpty()) {
                        return returnMessage(res, 500, "Something went wrong");
                    }
                    try {
                        removeAvailableTags(tagsRequest.getTags(), availableTags.get());
                    } catch (Throwable r) {
                        return returnMessage(res, 500, "Something went wrong, try again");
                    }
                    return returnMessage(res, 200, "Tags successfully removed");
                });
            });

            path("/users", () -> {
                get("", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        return returnMessage(res, 401, "Not logged in");
                    }
                    Long userId = getIdByToken(token);

                    Optional<User> user = findUserById(userId);
                    if (user.isEmpty()) {
                        return returnMessage(res, 500, "Something went wrong");
                    }

                    UserTagsResponse response = new UserTagsResponse(user.get().getLikedTags());
                    return returnMessage(res, 200, toJson(response));
                });

                delete("/delete", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        return returnMessage(res, 401, "Not logged in");
                    }
                    UserTagsRequest tagsRequest = fromJson(req.body(), UserTagsRequest.class);

                    Long userId = getIdByToken(token);

                    Optional<User> user = findUserById(userId);
                    if (user.isEmpty()) {
                        return returnMessage(res, 400, "User not found");
                    }

                    removeTagsFromUser(tagsRequest.getTags(), user.get());
                    try {
                        merge(user.get());
                        return returnMessage(res, 200, "Tags successfully removed");
                    } catch (Throwable e) {
                        return returnMessage(res, 500, "Something went wrong");
                    }
                });

                patch("/add", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        return returnMessage(res, 401, "Not logged in");
                    }
                    UserTagsRequest tagsRequest = fromJson(req.body(), UserTagsRequest.class);

                    Long userId = getIdByToken(token);
                    Optional<User> user = findUserById(userId);

                    if (!tagsExist(tagsRequest.getTags()) || user.isEmpty()) {
                        return returnMessage(res, 400, "Something went wrong");
                    }

                    addTagsToUser(tagsRequest.getTags(), user.get());

                    try {
                        merge(user.get());
                        return returnMessage(res, 200, "Tags successfully added");
                    } catch (Throwable e) {
                        return returnMessage(res, 500, "Something went wrong");
                    }
                });
            });

            path("/games", () -> {
                patch("/add/:gameId", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        return returnMessage(res, 401, "Not logged in");
                    }
                    UserTagsRequest tagsRequest = fromJson(req.body(), UserTagsRequest.class);

                    Optional<Game> game = findGameByIdJFTags(Long.valueOf(req.params(":gameId")));

                    if (!tagsExist(tagsRequest.getTags()) || game.isEmpty()) {
                        return returnMessage(res, 400, "Something went wrong");
                    }

                    Long userId = getIdByToken(token);

                    if (!Objects.equals(game.get().getCreatorId(), userId)) {
                        return returnMessage(res, 401, "Unauthorized");
                    }

                    addTagsToGame(tagsRequest.getTags(), game.get());

                    try {
                        merge(game.get());
                        return returnMessage(res, 200, "Tags added");
                    } catch (Throwable e) {
                        return returnMessage(res, 500, "Something went wrong");
                    }
                });

                delete("/delete/:gameId", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) {
                        return returnMessage(res, 401, "Not logged in");
                    }
                    UserTagsRequest tagsRequest = fromJson(req.body(), UserTagsRequest.class);

                    Optional<Game> game = findGameByIdJFTags(Long.valueOf(req.params(":gameId")));

                    if (!tagsExist(tagsRequest.getTags()) || game.isEmpty()) {
                        return returnMessage(res, 400, "Something went wrong");
                    }

                    Long userId = getIdByToken(token);

                    if (!Objects.equals(game.get().getCreatorId(), userId)) {
                        return returnMessage(res, 401, "Unauthorized");
                    }

                    removeTagsFromGame(tagsRequest.getTags(), game.get());

                    try {
                        merge(game.get());
                        return returnMessage(res, 200, "Tags removed");
                    } catch (Throwable e) {
                        return returnMessage(res, 500, "Something went wrong");
                    }
                });
            });

            get("/search/:searchTag", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                String searchTag = req.params(":searchTag");
                Optional<List<Game>> games = searchGameByTag(searchTag);
                if (games.isEmpty()) {
                    return returnMessage(res, 500, "Something went wrong");
                }
                List<GameResponse> gamesForResponse = getGameResponses(games.get());

                return toJson(new SearchTagResponse(gamesForResponse));
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.TAGS;
    }
}
