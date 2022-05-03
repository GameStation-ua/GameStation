package webpage.handlers;

import com.google.gson.Gson;
import webpage.model.*;
import webpage.requestFormats.CreateGameRequest;
import webpage.requestFormats.GameAprovalRequest;
import webpage.requestFormats.GameUpdateRequest;
import webpage.responseFormats.*;
import webpage.util.HandlerType;

import java.util.*;

import static spark.Spark.*;
import static webpage.entity.Games.*;
import static webpage.entity.Persister.*;
import static webpage.entity.Users.*;
import static webpage.responseFormats.SoftGameResponse.createSoftGameResponseList;
import static webpage.responseFormats.TagResponse.createTagResponseList;

public class ABMGamesHandler extends AbstractHandler{

    @Override
    public void handle() {

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



            post("/create", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                Long userId = getIdByToken(token);
                CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);

                Optional<GameRequest> gameRequest = createGameRequest(createGameRequest, userId);
                if (gameRequest.isEmpty()){
                    res.status(400);
                    return "{\"message\":\"Something went wrong.\"}";
                }

                try{
                    merge(gameRequest.get());
                    res.status(200);
                    return "{\"message\":\"OK.\"}";
                }catch (Exception e){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
            });

            post("/edit/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }

                Long gameId = Long.valueOf(req.params(":gameId"));
                Long userId = getIdByToken(token);

                if (!isOwner(userId, gameId)){
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
                CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);

                Optional<GameRequest> gameRequest = createGameRequest(createGameRequest, userId);
                if (gameRequest.isEmpty()){
                    res.status(400);
                    return "{\"message\":\"Something went wrong.\"}";
                }

                try{
                    merge(gameRequest.get());
                    res.status(200);
                    return "{\"message\":\"OK.\"}";
                }catch (Exception e){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
            });

            post("/approval", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }

                boolean isAdmin = getIsAdminByToken(token);
                if (!isAdmin){
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
                GameAprovalRequest gameAprovalRequest = new Gson().fromJson(req.body(), GameAprovalRequest.class);
                Optional<GameRequest> gameRequest = findGameRequestByIdJFTags(gameAprovalRequest.getGameRequestId());
                if (gameRequest.isEmpty()){
                    res.status(400);
                    return "{\"message\":\"Request not found.\"}";
                }
                if (!gameAprovalRequest.isApproved()){
                    remove(gameRequest.get());
                    res.status(200);
                    return "{\"message\":\"Request rejected.\"}";
                }
                try {
                    createGameFromRequest(gameRequest.get());
                }catch (Exception e){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
                res.status(200);
                return "{\"message\":\"Request accepted.\"}";
            });

            post("/gameupdate/add", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                Long userId = getIdByToken(token);
                GameUpdateRequest gameUpdateRequest = new Gson().fromJson(req.body(), GameUpdateRequest.class);
                if (!isOwner(userId, gameUpdateRequest.getGameId())){
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
                Optional<Game> game = findGameByIdJFFollowers(gameUpdateRequest.getGameId());
                if (game.isEmpty()){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
                Notification notification = new Notification(NotificationType.GAME_POSTED_UPDATE, game.get(), null, gameUpdateRequest.getPath());
                try {
                    createGameUpdate(gameUpdateRequest);
                    game.get().persistNotificationToFollowers(notification);
                    game.get().sendNotificationToFollowers(notification);
                    res.status(200);
                    return "{\"message\":\"OK.\"}";
                }catch (Exception e){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
            });

            get("/gamerequests", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }

                boolean isAdmin = getIsAdminByToken(token);
                if (!isAdmin){
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }

                Optional<List<GameRequestResponse>> gameResponses = getGameRequestsAsResponses();
                if (gameResponses.isEmpty()){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
                List<GameRequestResponse> gameRequestResponse = gameResponses.get();
                return new Gson().toJson(gameRequestResponse);
            });

            get("/info/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                Long gameId = Long.valueOf(req.params(":gameId"));

                Optional<Game> game = findGameByIdJFTags(gameId);
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
