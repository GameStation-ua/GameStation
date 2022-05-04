package webpage.entity;

import com.mortennobel.imagescaling.ResampleOp;
import spark.Request;
import spark.Response;
import webpage.model.Game;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mortennobel.imagescaling.ResampleFilters.getBiCubicHighFreqResponse;
import static java.nio.file.Files.move;
import static webpage.entity.Games.isOwner;
import static webpage.entity.Users.findCreatedGamesbyUserId;
import static webpage.entity.Users.getIdByToken;
import static webpage.handlers.AbstractHandler.returnMessage;

public class Uploads {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void saveImg(String directory, Request req) throws IOException, ServletException {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
            File file = new File(directory);
            copyInputStreamToFile(is, file);
        }
    }

    public static boolean rescale(String fromFile, String toFile, String width, String height) {
        String[] args = {fromFile, toFile, width, height};
        try {
            rescale(args);
        }catch (IOException e){
            return false;
        }
        return true;
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }

    public static boolean uploadAndRescale(Request req, Response res, String directory, String width, String height) throws IOException {
        try {
            saveImg(directory, req);
        if (rescale(directory, directory,width, height)) {
            return true;
        }
        }catch (ServletException e){
            return false;
        }
        return false;
    }

    public static String upload(Request req, Response res, String width, String height) {
        UUID uuid = UUID.randomUUID();
        String directory = "src/main/resources/public/temp/" + uuid + ".png";

        try {
            if (uploadAndRescale(req, res, directory, width, height)) {
                res.status(500);
                return returnMessage(res, 200, uuid.toString());
            }else {
                res.status(500);
                return returnMessage(res, 500, "Something went wrong");
            }
        }catch (Exception e){
            res.status(500);
            return returnMessage(res, 500, "Something went wrong");
        }
    }

    public static String uploadGameImg(Request req, Response res, String width, String height, String token) {
        Long userId = getIdByToken(token);
        Long gameid = Long.valueOf(req.headers("gameId"));

        Optional<List<Game>> gameList = findCreatedGamesbyUserId(userId);
        if (gameList.isEmpty()){
            res.status(500);
return returnMessage(res, 500, "Something went wrong");
        }
        if (isOwner(userId,gameid)) {
            return upload(req, res, width, height);
        }
        res.status(500);
return returnMessage(res, 401, "Unauthorized");
    }

    public static void rescale(String[] args) throws IOException {
        if (args.length != 4) {
            throw new IOException("Upload Error");
        }
        try {
            String sourcefile = args[0];
            String destfile = args[1];
            int newwidth = Integer.parseInt(args[2]);
            int newheight = Integer.parseInt(args[3]);

            BufferedImage sourceImage = ImageIO.read(new File(sourcefile));
            ResampleOp resizeOp = new ResampleOp(newwidth, newheight);
            resizeOp.setFilter(getBiCubicHighFreqResponse());
            BufferedImage resizedImage = resizeOp.filter(sourceImage, null);
            ImageIO.write(resizedImage, "png", new File(destfile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean attachMainImgToGame(Long gameId, String uuid){
        try {
            move(Paths.get("src/main/resources/public/temp/" + uuid + ".png"), Paths.get("src/main/resources/public/gameImages/" + gameId + "/gameMain.png"));
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
