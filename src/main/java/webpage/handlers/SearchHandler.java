package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import spark.Spark;
import webpage.entity.Game;
import webpage.entity.User;
import webpage.responseFormats.GameForResponse;
import webpage.responseFormats.SearchResponse;
import webpage.responseFormats.SearchTagResponse;
import webpage.responseFormats.UserForResponse;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.path;
import static webpage.util.SecretKey.key;

public class SearchHandler extends AbstractHandler{
    public SearchHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle(){
        path("/search", () -> {
           get("/:searchString", (req, res) -> {
               String searchTag = req.params(":searchString");
               String token = req.headers("token");
               if (verifyJWT(token)) {
                   Claims claims = Jwts.parser()
                           .setSigningKey(key)
                           .parseClaimsJws(token).getBody();
                   EntityManager em = emf.createEntityManager();
                   Query query = em.createQuery("FROM Game g WHERE g.title LIKE '%" + searchTag + "%'");
                   Query query1 = em.createQuery("FROM User u WHERE u.nickname LIKE '%" + searchTag + "%'");
                   @SuppressWarnings("unchecked") List<Game> games = query.getResultList();
                   @SuppressWarnings("unchecked") List<User> users = query1.getResultList();
                   List<GameForResponse> gamesForResponse = new ArrayList<>();
                   for (Game game : games) {
                       gamesForResponse.add(new GameForResponse(game));
                   }
                   List<UserForResponse> usersForResponse = new ArrayList<>();
                   for (User user : users) {
                       usersForResponse.add(new UserForResponse(user));
                   }
                   Gson gson = new Gson();
                   return gson.toJson(new SearchResponse(gamesForResponse,usersForResponse));
               }else {
                   res.status(401);
                   return "{\"message\":\"Not logged in.\"}";
               }
           });
        });
    }
}
