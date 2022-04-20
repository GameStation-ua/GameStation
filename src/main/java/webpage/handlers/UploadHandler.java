package webpage.handlers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Game;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static spark.Spark.path;
import static spark.Spark.post;
import static webpage.util.SecretKey.key;

public class UploadHandler extends AbstractHandler{

    public static final int DEFAULT_BUFFER_SIZE = 8192;


    public UploadHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle(){
        path("/upload", () -> {
            post("/profilepic", (request, response) -> {
                String token = request.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Integer userId = (Integer) claims.get("id");
                    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                    try {
                        try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                            File file = new File("src/main/resources/public/profile_pictures/" + userId + ".png");
                            copyInputStreamToFile(is, file);
                        }
                        response.status(200);
                        return "{\"message\":\"File uploaded.\"}";
                    } catch (NoResultException e) {
                        response.status(404);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                } else {
                    response.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
            });

            post("/public/gameMain", (request, response) -> {
                String token = request.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Integer userId = (Integer) claims.get("id");
                    String gameid = request.headers("gameId");
                    int gameid1 = Integer.parseInt(gameid);
                    EntityManager em = emf.createEntityManager();
                    @SuppressWarnings("unchecked") List<Game> gameList = em.createQuery("SELECT createdGames FROM User u WHERE u.id = :id")
                            .setParameter("id", userId)
                            .getResultList();
                    for (Game game : gameList) {
                        if (game.getId() == gameid1) {
                            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                            try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                                File file = new File("src/main/resources/public/gameMain/" + gameid1 + ".png");
                                copyInputStreamToFile(is, file);
                            }
                            return "{\"message\":\"File uploaded.\"}";
                        }
                    }
                    return "{\"message\":\"Unauthorized.\"}";
                } else {
                    response.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });

            post("/gameCarousel", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Integer userId1 = (Integer) claims.get("id");
                    String gameid = req.headers("gameId");
                    int gameid1 = Integer.parseInt(gameid);
                    EntityManager em = emf.createEntityManager();
                    @SuppressWarnings("unchecked") List<Game> gameList = em.createQuery("SELECT createdGames FROM User u WHERE u.id = :id")
                            .setParameter("id", userId1)
                            .getResultList();
                    em.close();
                    for (Game game : gameList) {
                        if (game.getId() == gameid1) {
                            int imgsInCarousel = game.getImgsInCarousel();
                            Path path1 = Paths.get("src/main/resources/public/Carousel=" + gameid1);
                            try {
                                Files.createDirectories(path1);
                            } catch (FileAlreadyExistsException ignored) {
                            }
                            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                            try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
                                File file = new File("src/main/resources/public/Carousel=" + gameid1 + "\\" + (imgsInCarousel + 1) + ".png");
                                copyInputStreamToFile(is, file);
                            }
                            game.setImgsInCarousel(imgsInCarousel + 1);
                            EntityManager em2 = emf.createEntityManager();
                            try{
                            em2.getTransaction().begin();
                            em2.merge(game);
                            em2.getTransaction().commit();
                            return "{\"message\":\"File uploaded.\"}";
                            }catch (Throwable r) {
                                em2.getTransaction().rollback();
                                res.status(500);
                                return "{\"message\":\"Something went wrong, try again.\"}";
                            }
                        }
                    }
                    return "{\"message\":\"Unauthorized.\"}";
                } else {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });
        });
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



