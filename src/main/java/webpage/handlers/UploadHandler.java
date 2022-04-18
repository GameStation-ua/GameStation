package webpage.handlers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Game;

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

    public void handle(String path){
        path(path, () -> {
            post("/profilepic", (request, response) -> {
                String token = request.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    String userId = (String) claims.get("id");
                    Integer userId1 = Integer.parseInt(userId);
                    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                    EntityManager em = emf.createEntityManager();
                    Query query = em.createQuery("FROM User u WHERE u.id = :id");
                    query.setParameter("id", userId1);
                    try {
                        try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                            File file = new File("C:\\Users\\jorge\\IdeaProjects\\GameStation\\src\\main\\resources\\public\\profile_pictures\\" + userId + ".png");
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

            post("/gameMain", (request, response) -> {
                String token = request.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    String userId = (String) claims.get("id");
                    Integer userId1 = Integer.parseInt(userId);
                    String gameid = request.headers("gameId");
                    int gameid1 = Integer.parseInt(gameid);
                    String imgType = request.headers("imgType");
                    EntityManager em = emf.createEntityManager();
                    Query query = em.createQuery("SELECT createdGames FROM User u WHERE u.id = :id");
                    query.setParameter("id", userId1);
                    @SuppressWarnings("unchecked") List<Game> gameList = query.getResultList();
                    boolean gameFound = false;
                    for (Game game : gameList) {
                        if (game.getId() == gameid1) {
                            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                            try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                                File file = new File("C:\\Users\\jorge\\IdeaProjects\\GameStation\\src\\main\\resources\\public\\gameMain\\" + gameid1 + ".png");
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

            post("/gameCarousel", (request, response) -> {
                String token = request.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    String userId = (String) claims.get("id");
                    Integer userId1 = Integer.parseInt(userId);
                    String gameid = request.headers("gameId");
                    int gameid1 = Integer.parseInt(gameid);
                    String imgType = request.headers("imgType");
                    EntityManager em = emf.createEntityManager();
                    Query query = em.createQuery("SELECT createdGames FROM User u WHERE u.id = :id");
                    query.setParameter("id", userId1);
                    @SuppressWarnings("unchecked") List<Game> gameList = query.getResultList();
                    em.close();
                    for (Game game : gameList) {
                        if (game.getId() == gameid1) {
                            int imgsInCarousel = 1;
                            Path path1 = Paths.get("C:\\Users\\jorge\\IdeaProjects\\GameStation\\src\\main\\resources\\public\\Carousel=" + gameid1);
                            try {
                                Files.createDirectories(path1);
                            } catch (FileAlreadyExistsException ignored) {
                            }
                            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                            try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                                File file = new File("C:\\Users\\jorge\\IdeaProjects\\GameStation\\src\\main\\resources\\public\\Carousel=" + gameid1 + "\\" + (imgsInCarousel + 1) + ".png");
                                copyInputStreamToFile(is, file);
                            }
                            game.setImgsInCarousel(imgsInCarousel + 1);
                            EntityManager em2 = emf.createEntityManager();
                            em2.getTransaction().begin();
                            em2.merge(game);
                            em2.getTransaction().commit();
                            return "{\"message\":\"File uploaded.\"}";
                        }
                    }
                    return "{\"message\":\"Unauthorized.\"}";
                } else {
                    response.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });
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



