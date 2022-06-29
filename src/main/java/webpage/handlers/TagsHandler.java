package webpage.handlers;

import webpage.entity.Games;
import webpage.model.Game;
import webpage.model.Tag;
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
                get("","application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                    Optional<List<Tag>> availableTags = findAvailableTags();
                    if (availableTags.isEmpty()) return returnJson(res, 500, "Something went wrong");
                    AvailableTagsResponse response = new AvailableTagsResponse(availableTags.get());
                    res.status(200);
                    return toJson(response);
                });

                patch("/add", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

                    boolean isAdmin = getIsAdminByToken(token);
                    if (!isAdmin) return returnJson(res, 401, "Unauthorized");

                    AvailableTagsRequest tagsRequest = fromJson(req.body(), AvailableTagsRequest.class);
                    if (tagsRequest.getTags().isEmpty()) return returnJson(res, 500, "Something went wrong, try again");

                    try {
                        addTags(tagsRequest.getTags());
                    } catch (Throwable r) {
                        return returnJson(res, 500, "Something went wrong, try again");
                    }
                    return returnJson(res, 200, "Tags are now in the system");
                });

                delete("/delete", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

                    boolean isAdmin = getIsAdminByToken(token);
                    if (!isAdmin) return returnJson(res, 401, "Unauthorized");

                    AvailableTagsRequest tagsRequest = fromJson(req.body(), AvailableTagsRequest.class);
                    if (tagsRequest.getTags().isEmpty()) return returnJson(res, 500, "Something went wrong, try again");

                    try {
                        removeTags(tagsRequest.getTags());
                    } catch (Throwable r) {
                        return returnJson(res, 500, "Something went wrong, try again");
                    }
                    return returnJson(res, 200, "Tags successfully removed");
                });
            });

            path("/users", () -> {
                get("","application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                    Long userId = getIdByToken(token);

                    Optional<User> user = findUserById(userId);
                    if (user.isEmpty()) return returnJson(res, 500, "Something went wrong");

                    UserTagsResponse response = new UserTagsResponse(user.get().getLikedTags());
                    res.status(200);
                    return toJson(response);
                });

                delete("/delete", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                    UserTagsRequest tagsRequest = fromJson(req.body(), UserTagsRequest.class);

                    Long userId = getIdByToken(token);

                    Optional<User> user = findUserById(userId);
                    if (user.isEmpty()) return returnJson(res, 400, "User not found");
                    Optional<List<Tag>> requestTags = findTagsIfAvailable(tagsRequest.getTags());
                    if (requestTags.isEmpty()) return returnJson(res, 500, "Server error, try again");

                    removeTagsFromUser(requestTags.get(), user.get());
                    try {
                        merge(user.get());
                        return returnJson(res, 200, "Tags successfully removed");
                    } catch (Throwable e) {
                        return returnJson(res, 500, "Something went wrong");
                    }
                });

                patch("/add", "application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                    UserTagsRequest tagsRequest = fromJson(req.body(), UserTagsRequest.class);

                    Long userId = getIdByToken(token);
                    Optional<User> user = findUserById(userId);

                    if (!tagsExist(tagsRequest.getTags()) || user.isEmpty()) {
                        return returnJson(res, 400, "Something went wrong");
                    }
                    Optional<List<Tag>> requestTags = findTagsIfAvailable(tagsRequest.getTags());
                    if (requestTags.isEmpty()) return returnJson(res, 500, "Server error, try again");


                    addTagsToUser(requestTags.get(), user.get());

                    try {
                        merge(user.get());
                        return returnJson(res, 200, "Tags successfully added");
                    } catch (Throwable e) {
                        return returnJson(res, 500, "Something went wrong");
                    }
                });
            });

            path("/games", () -> {
                patch("/add/:gameId","application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                    UserTagsRequest tagsRequest = fromJson(req.body(), UserTagsRequest.class);

                    Optional<Game> game = Games.findGameById(Long.valueOf(req.params(":gameId")));

                    if (!tagsExist(tagsRequest.getTags()) || game.isEmpty()) return returnJson(res, 400, "Something went wrong");

                    Long userId = getIdByToken(token);

                    if (!Objects.equals(game.get().getCreatorId(), userId)) {
                        return returnJson(res, 401, "Unauthorized");
                    }
                    Optional<List<Tag>> requestTags = findTagsIfAvailable(tagsRequest.getTags());
                    if (requestTags.isEmpty()) return returnJson(res, 500, "Server error, try again");

                    addTagsToGame(requestTags.get(), game.get());

                    try {
                        merge(game.get());
                        return returnJson(res, 200, "Tags added");
                    } catch (Throwable e) {
                        return returnJson(res, 500, "Something went wrong");
                    }
                });

                delete("/delete/:gameId","application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                    UserTagsRequest tagsRequest = fromJson(req.body(), UserTagsRequest.class);

                    Optional<Game> game = Games.findGameById(Long.valueOf(req.params(":gameId")));

                    if (!tagsExist(tagsRequest.getTags()) || game.isEmpty()) return returnJson(res, 400, "Something went wrong");

                    Long userId = getIdByToken(token);

                    if (!Objects.equals(game.get().getCreatorId(), userId)) return returnJson(res, 401, "Unauthorized");

                    Optional<List<Tag>> requestTags = findTagsIfAvailable(tagsRequest.getTags());
                    if (requestTags.isEmpty()) return returnJson(res, 500, "Server error, try again");

                    removeTagsFromGame(requestTags.get(), game.get());

                    try {
                        merge(game.get());
                        return returnJson(res, 200, "Tags removed");
                    } catch (Throwable e) {
                        return returnJson(res, 500, "Something went wrong");
                    }
                });
            });

            get("/search/:searchTag","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                String searchTag = req.params(":searchTag");

                Optional<List<Game>> games = search50GamesByTag(searchTag);
                if (games.isEmpty()) return returnJson(res, 500, "Something went wrong");

                List<GameResponse> gamesForResponse = getGameResponses(games.get(), getIdByToken(token));

                res.status(200);
                return toJson(new SearchTagResponse(gamesForResponse));
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.TAGS;
    }
}
