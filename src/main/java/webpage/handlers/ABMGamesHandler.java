package webpage.handlers;

import webpage.model.*;
import webpage.requestFormats.CreateGameRequest;
import webpage.requestFormats.GameAprovalRequest;
import webpage.requestFormats.GameUpdateRequest;
import webpage.responseFormats.*;
import webpage.util.HandlerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Games.*;
import static webpage.entity.Persister.merge;
import static webpage.entity.Persister.remove;
import static webpage.entity.Users.*;
import static webpage.responseFormats.SoftGameResponse.createSoftGameResponseList;
import static webpage.responseFormats.TagResponse.createTagResponseList;
import static webpage.util.Parser.fromJson;
import static webpage.util.Parser.toJson;

public class ABMGamesHandler extends AbstractHandler{

    @Override
    public void handle() {

        path("/game", () -> {
            get("/createdgames", (req, res) ->{
                String token = req.headers("token");

                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);

                Optional<List<Game>> games = findCreatedGamesbyUserId(userId);
                if (games.isEmpty()){
                    return returnMessage(res, 500, "Something went wrong");
                }
                List<SoftGameResponse> softGameResponseList = createSoftGameResponseList(games.get());

                return toJson(softGameResponseList);
            });



            post("/create", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                CreateGameRequest createGameRequest = fromJson(req.body(), CreateGameRequest.class);

                Optional<GameRequest> gameRequest = createGameRequest(createGameRequest, userId);
                if (gameRequest.isEmpty()){
                    return returnMessage(res, 400, "Something went wrong");
                }

                try{
                    merge(gameRequest.get());
                    return returnMessage(res, 200, "OK");
                }catch (Exception e){
                    return returnMessage(res, 500, "Something went wrong");
                }
            });

            post("/edit/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }

                Long gameId = Long.valueOf(req.params(":gameId"));
                Long userId = getIdByToken(token);

                if (!isOwner(userId, gameId)){
                    return returnMessage(res, 401, "Unauthorized");
                }
                CreateGameRequest createGameRequest = fromJson(req.body(), CreateGameRequest.class);

                Optional<GameRequest> gameRequest = createGameRequest(createGameRequest, userId);
                if (gameRequest.isEmpty()){
                    return returnMessage(res, 400, "Something went wrong");
                }

                try{
                    merge(gameRequest.get());
                    return returnMessage(res, 200, "OK");
                }catch (Exception e){
                    return returnMessage(res, 500, "Something went wrong");
                }
            });

            post("/approval", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }

                boolean isAdmin = getIsAdminByToken(token);
                if (!isAdmin){
                    return returnMessage(res, 401, "Unauthorized");
                }
                GameAprovalRequest gameAprovalRequest = fromJson(req.body(), GameAprovalRequest.class);
                Optional<GameRequest> gameRequest = findGameRequestByIdJFTags(gameAprovalRequest.getGameRequestId());
                if (gameRequest.isEmpty()){
                    return returnMessage(res, 400, "Request not found");
                }
                if (!gameAprovalRequest.isApproved()){
                    remove(gameRequest.get());
                    return returnMessage(res, 200, "Request rejected");
                }
                try {
                    createGameFromRequest(gameRequest.get());
                }catch (Exception e){
                    return returnMessage(res, 500, "Something went wrong");
                }
                return returnMessage(res, 200, "Request accepted");
            });

            post("/gameupdate/add", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                GameUpdateRequest gameUpdateRequest = fromJson(req.body(), GameUpdateRequest.class);
                if (!isOwner(userId, gameUpdateRequest.getGameId())){
                    return returnMessage(res, 401, "Unauthorized");
                }
                Optional<Game> game = findGameByIdJFFollowers(gameUpdateRequest.getGameId());
                if (game.isEmpty()){
                    return returnMessage(res, 500, "Something went wrong");
                }
                Notification notification = new Notification(NotificationType.GAME_POSTED_UPDATE, game.get(), null, gameUpdateRequest.getPath());
                try {
                    createGameUpdate(gameUpdateRequest);
                    game.get().persistNotificationToFollowers(notification);
                    game.get().sendNotificationToFollowers(notification);
                    return returnMessage(res, 200, "OK");
                }catch (Exception e){
                    return returnMessage(res, 500, "Something went wrong");
                }
            });

            get("/gamerequests", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }

                boolean isAdmin = getIsAdminByToken(token);
                if (!isAdmin){
                    return returnMessage(res, 401, "Unauthorized");
                }

                Optional<List<GameRequestResponse>> gameResponses = getGameRequestsAsResponses();
                if (gameResponses.isEmpty()){
                    return returnMessage(res, 500, "Something went wrong");
                }
                List<GameRequestResponse> gameRequestResponse = gameResponses.get();
                return toJson(gameRequestResponse);
            });

            get("/info/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long gameId = Long.valueOf(req.params(":gameId"));

                Optional<Game> game = findGameByIdJFTags(gameId);
                Optional<List<UserGame>> userGames = findUserGamesbyGameId(gameId);
                if (game.isEmpty() || userGames.isEmpty()){
                    return returnMessage(res, 400, "Something went wrong");
                }
                float meanScore = getMeanScore(userGames.get());
                Optional<User> creator = findUserById(game.get().getCreatorId());
                if (creator.isEmpty()){
                    return returnMessage(res, 400, "Something went wrong");
                }
                UserResponse userResponse = new UserResponse(creator.get());
                List<TagResponse> tagsForResponse = createTagResponseList(new ArrayList<>(game.get().getTags()));

                HardGameForResponse gameForResponse = new HardGameForResponse(gameId, meanScore, game.get().getFollowers().size(), game.get().getTitle(), game.get().getDescription(), game.get().getImgsInCarousel(), game.get().getWiki(), userResponse, tagsForResponse, game.get().getGameUpdates());
                return returnMessage(res, 200, toJson(gameForResponse));
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
