package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.requestFormats.ImageUploadRequest;

import javax.persistence.EntityManagerFactory;
import javax.servlet.MultipartConfigElement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static spark.Spark.post;
import static webpage.util.SecretKey.key;

public class ImageHandler extends AbstractHandler{

    public static final int DEFAULT_BUFFER_SIZE = 8192;


    public ImageHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle(String path){
        post("/upload", (request, response) -> {
            String token = request.headers("token");
            if (verifyJWT(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                String userId = (String) claims.get("id");
                Integer userId1 = Integer.parseInt(userId);
                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                String imgType = request.headers("imgType");
                try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                    File file = new File("C:\\Users\\jorge\\IdeaProjects\\GameStation\\src\\main\\resources\\public\\profile_pictures\\" + userId + "." + imgType);
                    copyInputStreamToFile(is, file);
                }
                return "{\"message\":\"File uploaded.\"}";
            }else{
                response.status(401);
                return "{\"message\":\"Unauthorized.\"}";
            }
        });
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
