package webpage.handlers;

import spark.Request;
import spark.Response;
import webpage.model.Game;
import webpage.util.HandlerType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static spark.Spark.path;
import static spark.Spark.post;
import static webpage.entity.Games.isOwner;
import static webpage.entity.Uploads.uploadAndRescale;
import static webpage.entity.Uploads.uploadGameImg;
import static webpage.entity.Users.findCreatedGamesbyUserId;
import static webpage.entity.Users.getIdByToken;

public class UploadHandler extends AbstractHandler{

    public void handle(){
        path("/upload", () -> {
            post("/profilepic", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                Long userId = getIdByToken(token);
                String directory = "src/main/resources/public/profile_pictures/" + userId + ".png";

                String width = "256";
                String height = "256";

                if(uploadAndRescale(req, res, directory, width, height)){
                    res.status(200);
                    return "{\"message\":\"OK.\"}";
                }
                res.status(500);
                return "{\"message\":\"Something went wrong.\"}";
            });

            post("/gameMain", (req, res) -> {
                String width = "256";
                String height = "256";

                return verifyAndUploadGameImg(req, res, width, height);
            });

            post("/gameCarousel", (req, res) -> {
                String width = "256";
                String height = "256";

                return verifyAndUploadGameImg(req, res, width, height);
            });

            post("/gameupdate", (req, res) -> {
                String width = "256";
                String height = "256";

                return verifyAndUploadGameImg(req, res, width, height);
            });
        });
    }



    private String verifyAndUploadGameImg(Request req, Response res, String width, String height){
        String token = req.headers("token");
        if (!verifyJWT(token)) {
            res.status(401);
            return "{\"message\":\"Not logged in.\"}";
        }
        return uploadGameImg(req, res, width, height, token);
    }


    @Override
    public HandlerType getType() {
        return HandlerType.UPLOAD;
    }

}



