package webpage.handlers;

import spark.Request;
import spark.Response;
import webpage.util.HandlerType;

import static spark.Spark.path;
import static spark.Spark.post;
import static webpage.entity.Uploads.uploadAndRescale;
import static webpage.entity.Uploads.uploadGameImg;
import static webpage.entity.Users.getIdByToken;

public class UploadHandler extends AbstractHandler{

    public void handle(){
        path("/upload", () -> {
            post("/profilepic", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                Long userId = getIdByToken(token);
                String directory = "src/main/resources/public/profile_pictures/" + userId + ".png";

                String width = "256";
                String height = "256";

                if(uploadAndRescale(req, res, directory, width, height)){
                    return returnMessage(res, 200, "OK");
                }
                return returnMessage(res, 500, "Something went wrong");
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
            return returnMessage(res, 401, "Not logged in");
        }
        return uploadGameImg(req, res, width, height, token);
    }


    @Override
    public HandlerType getType() {
        return HandlerType.UPLOAD;
    }

}



