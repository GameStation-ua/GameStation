package webpage.handlers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import spark.Request;
import spark.Response;
import webpage.model.Game;
import webpage.util.HandlerType;
import webpage.util.ImageRescaler;

import javax.persistence.NoResultException;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static spark.Spark.path;
import static spark.Spark.post;
import static webpage.entity.Persister.merge;
import static webpage.entity.Users.findCreatedGamesbyUserId;
import static webpage.entity.Users.getIdByToken;
import static webpage.util.EntityManagers.createEntityManager;
import static webpage.util.SecretKey.key;

public class UploadHandler extends AbstractHandler{

    public static final int DEFAULT_BUFFER_SIZE = 8192;

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

                return uploadAndRescale(req, res, userId, directory);
            });

            post("/gameMain", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                    Long userId = getIdByToken(token);
                    Long gameid = Long.valueOf(req.headers("gameId"));
                    String directory = "src/main/resources/public/gameImages/" + gameid + "/gameMain.png";

                    
                    Optional<List<Game>> gameList = findCreatedGamesbyUserId(userId);
                    if (gameList.isEmpty()){
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                    for (Game game : gameList.get()) {
                        if (game.getId().equals(gameid)) {
                            return uploadAndRescale(req, res, userId, directory);
                        }
                    }
                    return "{\"message\":\"Unauthorized.\"}";
            });

            post("/gameCarousel", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                Long userId = getIdByToken(token);
                Long gameid = Long.valueOf(req.headers("gameId"));

                Optional<List<Game>> gameList = findCreatedGamesbyUserId(userId);
                if (gameList.isEmpty()){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
                for (Game game : gameList.get()) {
                    if (game.getId().equals(gameid)) {
                        int imgsInCarousel = game.getImgsInCarousel();
                        String directory = "src/main/resources/public/gameImages/" + gameid + "/Carousel=" + (imgsInCarousel + 1) + ".png";
                        uploadAndRescale(req, res, userId, directory);
                        game.setImgsInCarousel(imgsInCarousel + 1);
                        try{
                        merge(game);
                        return "{\"message\":\"File uploaded.\"}";
                        }catch (Throwable r) {
                            res.status(500);
                            return "{\"message\":\"Something went wrong, try again.\"}";
                        }
                    }
                }
                res.status(401);
                return "{\"message\":\"Unauthorized.\"}";
            });
        });
    }

    private String uploadAndRescale(Request req, Response res, Long userId, String directory) throws IOException {
        try {
            saveImg(directory, req, userId);
        }catch (ServletException e){
            res.status(400);
            return "{\"message\":\"Wrong format.\"}";
        }

        if (rescale(directory, directory,"256", "256", userId)) {
            res.status(200);
            return "{\"message\":\"File uploaded.\"}";
        }
        res.status(500);
        return "{\"message\":\"Something went wrong.\"}";
    }

    private void saveImg(String directory, Request req, Long userId) throws IOException, ServletException {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
            File file = new File(directory);
            copyInputStreamToFile(is, file);
        }
    }

    private boolean rescale(String fromFile, String toFile, String width, String height, Long userId) {
        ImageRescaler imageRescaler = new ImageRescaler();
        String[] args = {fromFile, toFile, width, height};
        try {
            imageRescaler.rescale(args);
        }catch (IOException e){
            return false;
        }
        return true;
    }

    @Override
    public HandlerType getType() {
        return HandlerType.UPLOAD;
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }


}



