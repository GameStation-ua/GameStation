package webpage.handlers;

import com.google.gson.Gson;
import webpage.entity.Game;
import webpage.entity.User;
import webpage.responseFormats.GameForResponse;
import webpage.responseFormats.SearchResponse;
import webpage.responseFormats.UserForResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.path;

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
                   EntityManager em = emf.createEntityManager();
                   @SuppressWarnings("unchecked") List<Game> games = em.createQuery("FROM Game g WHERE UPPER(g.title) LIKE ?1")
                           .setParameter(1, "%" + searchTag.toUpperCase() + "%")
                           .getResultList();
                   @SuppressWarnings("unchecked") List<User> users = em.createQuery("FROM User u WHERE UPPER(u.nickname) LIKE ?1")
                           .setParameter(1, "%" + searchTag.toUpperCase() + "%")
                           .getResultList();
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

    @Override
    public HandlerType getType() {
        return HandlerType.SEARCH;
    }
}
