package webpage.handlers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import webpage.model.*;
import webpage.requestFormats.CreateGameRequest;
import webpage.requestFormats.EditGameRequest;
import webpage.requestFormats.GameAprovalRequest;
import webpage.requestFormats.GameUpdateRequest;
import webpage.responseFormats.*;
import webpage.util.HandlerType;
import webpage.util.NotificationType;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.api.DiscordBot.sendNews;
import static webpage.entity.Actors.persistNotificationToFollowers;
import static webpage.entity.Actors.sendNotificationToFollowers;
import static webpage.entity.Games.*;
import static webpage.entity.Images.moveImagesFromRequest;
import static webpage.entity.Images.upload;
import static webpage.entity.Persister.merge;
import static webpage.entity.Persister.remove;
import static webpage.entity.Tags.createTagResponseList;
import static webpage.entity.Users.*;
import static webpage.util.Parser.fromJson;
import static webpage.util.Parser.toJson;
import static webpage.util.ServerInitializer.imagesPath;

public class ABMGamesHandler extends AbstractHandler{

    @Override
    public void handle() {

        path("/game", () -> {

            path("/solicitude", () -> {

                post("/create","application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                    Long userId = getIdByToken(token);
                    CreateGameRequest createGameRequest = fromJson(req.body(), CreateGameRequest.class);

                    Optional<GameRequest> gameRequest = createGameRequest(createGameRequest, userId);
                    if (gameRequest.isEmpty()) return returnJson(res, 400, "Something went wrong");

                    try {
                        GameRequest gameRequest1 = merge(gameRequest.get());
                        Files.createDirectories(Paths.get(imagesPath + "/game_requests/" + gameRequest1.getId()));
                        res.status(200);
                        return "" + gameRequest1.getId();
                    } catch (Exception e) {
                        return returnJson(res, 500, "Something went wrong");
                    }
                });

                post("/edit/:gameId","application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

                    Long gameId = Long.valueOf(req.params(":gameId"));
                    Long userId = getIdByToken(token);

                    if (!isOwner(userId, gameId)) return returnJson(res, 401, "Unauthorized");
                    EditGameRequest editGameRequest = fromJson(req.body(), EditGameRequest.class);
                    editGameRequest.setGameId(gameId);

                    Optional<GameRequest> gameRequest = editGameRequest(editGameRequest, userId, gameId);
                    if (gameRequest.isEmpty()) return returnJson(res, 400, "Something went wrong");

                    try{
                        GameRequest gameRequest1 = merge(gameRequest.get());
                        Files.createDirectories(Paths.get(imagesPath + "/game_requests/" + gameRequest1.getId()));
                        res.status(200);
                        return  "" + gameRequest1.getId();
                    }catch (Exception e){
                        return returnJson(res, 500, "Something went wrong");
                    }
                });

                post("/approval","application/json", (req, res) -> {
                    String token = req.headers("token");
                    if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

                    boolean isAdmin = getIsAdminByToken(token);
                    if (!isAdmin) return returnJson(res, 401, "Unauthorized");
                    GameAprovalRequest gameAprovalRequest = fromJson(req.body(), GameAprovalRequest.class);
                    Optional<GameRequest> gameRequest = findGameRequestById(gameAprovalRequest.getGameRequestId());
                    if (gameRequest.isEmpty())return returnJson(res, 400, "Request not found");

                    if (!gameAprovalRequest.isApproved()){
                        remove(gameRequest.get());
                        return returnJson(res, 200, "Request rejected");
                    }
                    if (!gameRequest.get().isAlreadyExists()) {
                        try {
                            Game game = createGameFromRequest(gameRequest.get());
                            FileUtils.moveDirectory(new File(imagesPath + "/game_requests/" + gameRequest.get().getId()).getAbsoluteFile(), new File(imagesPath + "/games/" + game.getId()).getAbsoluteFile());
                        } catch (Exception e) {
                            return returnJson(res, 500, "Something went wrong");
                        }
                        return returnJson(res, 200, "Request accepted");
                    }
                    try {
                        Long gameId = editGame(gameRequest.get());
                        moveImagesFromRequest(new File(imagesPath + "/game_requests/" + gameRequest.get().getId()).getAbsoluteFile(), new File(imagesPath + "/games/" + gameId).getAbsoluteFile());
                    }catch (Exception e){
                        return returnJson(res, 500, "Something went wrong");
                    }
                    return returnJson(res, 200, "Request accepted");
                });
            });

            get("/createdgames/:userId","application/json", (req, res) ->{
                String token = req.headers("token");

                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = Long.valueOf(req.params(":userId"));

                Optional<List<Game>> games = findCreatedGamesbyUserId(userId);
                if (games.isEmpty()) return returnJson(res, 500, "Something went wrong");
                List<SoftGameResponse> softGameResponseList = createSoftGameResponseList(games.get(), getIdByToken(token));
                res.status(200);
                return toJson(softGameResponseList);
            });

            post("/gameupdate/add","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                GameUpdateRequest gameUpdateRequest = fromJson(IOUtils.toString(req.raw().getPart("json").getInputStream(), StandardCharsets.UTF_8), GameUpdateRequest.class);
                if (!isOwner(userId, gameUpdateRequest.getGameId())) return returnJson(res, 401, "Unauthorized");
                Optional<Game> game = findGameById(gameUpdateRequest.getGameId());
                if (game.isEmpty()) return returnJson(res, 500, "Something went wrong");
                Notification notification = new Notification(NotificationType.GAME_POSTED_UPDATE, game.get(), null, gameUpdateRequest.getPath());
                try {
                    GameUpdate gameUpdate = createGameUpdate(gameUpdateRequest);
                    persistNotificationToFollowers(notification, game.get());
                    upload(req, res, 960, 540, imagesPath + "/game_updates/" + gameUpdate.getId() + ".png");
                    sendNotificationToFollowers(notification, game.get());
                    sendNews(game.get(),gameUpdateRequest, gameUpdate.getId());
                    return returnJson(res, 200, "OK");
                }catch (Exception e){
                    return returnJson(res, 500, "Something went wrong");
                }
            });

            get("/gamerequests","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

                boolean isAdmin = getIsAdminByToken(token);
                if (!isAdmin) return returnJson(res, 401, "Unauthorized");

                Optional<List<GameRequestResponse>> gameResponses = getGameRequestsAsResponses();
                if (gameResponses.isEmpty()) return returnJson(res, 500, "Something went wrong");
                List<GameRequestResponse> gameRequestResponse = gameResponses.get();
                res.status(200);
                return toJson(gameRequestResponse);
            });

            get("/info/:gameId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long gameId = Long.valueOf(req.params(":gameId"));

                Optional<Game> game = findGameById(gameId);
                if(game.isEmpty()) return returnJson(res, 400, "Something went wrong");

                Optional<List<UserGame>> userGames = findUserGamesbyGameId(gameId);
                if (userGames.isEmpty()) return returnJson(res, 400, "Something went wrong");

                float meanScore = getMeanScore(userGames.get());
                Optional<User> creator = findUserById(game.get().getCreatorId());
                if (creator.isEmpty()) return returnJson(res, 400, "Something went wrong");

                UserResponse userResponse = new UserResponse(creator.get(), getIdByToken(token));
                List<String> tagsForResponse = createTagResponseList(new ArrayList<>(game.get().getTags()));
                Long userId = getIdByToken(token);

                HardGameForResponse gameForResponse;
                try {
                    gameForResponse = new HardGameForResponse(gameId, meanScore, game.get().getFollowers().size(), game.get().getTitle(), game.get().getDescription(), game.get().getImgsInCarousel(), game.get().getWiki(), userResponse, tagsForResponse, userId);
                }catch (Exception e){
                    gameForResponse = new HardGameForResponse(gameId, meanScore, 0, game.get().getTitle(), game.get().getDescription(), game.get().getImgsInCarousel(), game.get().getWiki(), userResponse, tagsForResponse, userId);
                }
                res.status(200);
                return toJson(gameForResponse);
            });

            get("/gameUpdate/*/*","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                String[] request = req.splat();
                Long gameId = Long.valueOf(request[0]);
                int pageNumber = Integer.parseInt(request[1]);

                Optional<List<GameUpdate>> gameUpdates = findGameUpdatesByPage(gameId, pageNumber);
                if (gameUpdates.isEmpty()) return returnJson(res, 500, "Something went wrong");

                List<GameUpdateResponse> gameUpdateResponseList = prepareGameUpdatesResponse(gameUpdates.get());

                return toJson(gameUpdateResponseList);
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
            meanScore = 0;
        }
        return meanScore;
    }

    @Override
    public HandlerType getType() {
        return HandlerType.ABM_GAMES;
    }
}
