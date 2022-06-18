package webpage.handlers;

import webpage.model.Game;
import webpage.model.GameRequest;
import webpage.model.GameUpdate;
import webpage.util.HandlerType;
import webpage.util.ImgType;

import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Games.*;
import static webpage.entity.Images.findImg;
import static webpage.entity.Images.upload;
import static webpage.entity.Persister.merge;
import static webpage.entity.Users.getIdByToken;
import static webpage.util.Parser.fromJson;
import static webpage.util.ServerInitializer.ImagesPath;

public class ImageHandler extends AbstractHandler{

    public void handle(){
        path("/upload", () -> post("/attachImg","application/json", (req, res) -> {
            String token = req.headers("token");
            if (!verifyJWT(token)) return returnMessage(res, 401, "Not logged in");
            ImgType imgType = fromJson(req.headers("imgType"), ImgType.class);

            switch (imgType) {
                case MAIN:
                    Optional<GameRequest> gameRequest = findGameRequestById(Long.valueOf(req.headers("id")));
                    if (gameRequest.isEmpty() || !(gameRequest.get().getCreatorId().equals(getIdByToken(token)))) return returnMessage(res, 401, "Unauthorized");
                    return upload(req, res, 960, 540, ImagesPath + "/game_requests/" + req.headers("id") + "/main.png");


                case CAROUSEL:
                    Optional<GameRequest> gameRequest1 = findGameRequestById(Long.valueOf(req.headers("id")));
                    if (gameRequest1.isEmpty() || !(gameRequest1.get().getCreatorId().equals(getIdByToken(token)))) return returnMessage(res, 401, "Unauthorized");
                    String returnMessage = upload(req, res, 960, 540, ImagesPath + "/game_requests/" + req.headers("id") + "/carousel=" + (gameRequest1.get().getImgsInCarousel() + 1) + ".png");
                    gameRequest1.get().setImgsInCarousel(gameRequest1.get().getImgsInCarousel() + 1);
                    merge(gameRequest1.get());
                    return returnMessage;


                case GAME_UPDATE:
                    Optional<GameUpdate> gameUpdate = findGameUpdateById(Long.valueOf(req.headers("id")));
                    if (gameUpdate.isEmpty()) return returnMessage(res, 500, "Something went wrong");
                    Optional<Game> game = findGameById(gameUpdate.get().getGameId());
                    if (game.isEmpty()) return returnMessage(res, 500, "Something went wrong");
                    if (!(game.get().getCreatorId().equals(getIdByToken(token)))) return returnMessage(res, 401, "Unauthorized");

                    return upload(req, res, 960, 540, ImagesPath + "/game_updates/" + gameUpdate.get().getId() + ".png");


                case PROFILE:
                    return upload(req, res, 540, 540, ImagesPath + "/profile_pictures/" + getIdByToken(token) + ".png");


                default: return returnMessage(res, 500, "Something went wrong");
            }
        }));
        get("/image","image/png", (req, res) -> {
            String token = req.headers("token");
            if (!verifyJWT(token)) return returnMessage(res, 401, "Not logged in");

            try {
                res.status(200);
                return findImg(req.headers("path"));
            }catch (Exception e){
                return returnMessage(res, 500, "Something went wrong");
            }
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.UPLOAD;
    }


    private String[] decodePath(String rawPath){
        if (rawPath == null) return null;
        return rawPath.split("/");
    }
}



