package webpage.handlers;


import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Game;
import webpage.entity.Tag;
import webpage.entity.User;
import webpage.responseFormats.GameForResponse;
import webpage.responseFormats.HomeResponse;
import webpage.responseFormats.TagForResponse;
import webpage.responseFormats.UserForResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static webpage.util.SecretKey.key;

public class HomeHandler extends AbstractHandler{
    public HomeHandler(EntityManagerFactory emf) {
        super(emf);
    }


    public void handle(){

        get("/home", (req, res) -> {
            String token = req.headers("token");
            if (verifyJWT(token)){
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                Integer userId = (Integer) claims.get("id");
                EntityManager em = emf.createEntityManager();
                Query query = em.createQuery("FROM User u WHERE u.id = ?1");
                query.setParameter(1, userId);
                User user = (User) query.getSingleResult();
                UserForResponse userForResponse = new UserForResponse(user);            // User ready
                Query query1 = em.createQuery("SELECT likedTags FROM User u WHERE u.id = ?1");
                query1.setParameter(1, userId);
                @SuppressWarnings("unchecked") List<Tag> tags = (List<Tag>) query1.getResultList();
                fillTags(em, tags);                                                    // Tags ready
                Query query2 = em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1").setMaxResults(5);
                query2.setParameter(1, tags.get(0).getName());
                @SuppressWarnings("unchecked")List<Game> gamesTag1 = (List<Game>) query2.getResultList();
                List<GameForResponse> gamesForResponse1 = gameForResponseList(gamesTag1);
                Query query3 = em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1").setMaxResults(5);
                query3.setParameter(1, tags.get(1).getName());
                @SuppressWarnings("unchecked")List<Game> gamesTag2 = (List<Game>)query3.getResultList();
                List<GameForResponse> gamesForResponse2 = gameForResponseList(gamesTag2);
                Query query4 = em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1").setMaxResults(5);
                query4.setParameter(1, tags.get(2).getName());
                @SuppressWarnings("unchecked")List<Game> gamesTag3 = (List<Game>)query4.getResultList();
                List<GameForResponse> gamesForResponse3 = gameForResponseList(gamesTag3);
                Query query5 = em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1").setMaxResults(5);
                query5.setParameter(1, tags.get(3).getName());
                @SuppressWarnings("unchecked")List<Game> gamesTag4 = (List<Game>)query5.getResultList();
                List<GameForResponse> gamesForResponse4 = gameForResponseList(gamesTag4);
                Query query6 = em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1").setMaxResults(5);
                query6.setParameter(1, tags.get(4).getName());
                @SuppressWarnings("unchecked")List<Game> gamesTag5 = (List<Game>)query6.getResultList();
                List<GameForResponse> gamesForResponse5 = gameForResponseList(gamesTag5);
                List<TagForResponse> tagForResponseList = tagForResponseList(tags);
                HomeResponse homeResponse = new HomeResponse(userForResponse, tagForResponseList, gamesForResponse1, gamesForResponse2, gamesForResponse3, gamesForResponse4, gamesForResponse5);
                Gson gson = new Gson();
                res.status(200);
                return gson.toJson(homeResponse);
            }else{
                res.status(401);
                return "{\"message\":\"Not logged in.\"}";
            }
        });

        get("/isAdmin", (req, res) -> {
            String token = req.headers("token");
            if (verifyJWT(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                Boolean isAdmin = (Boolean) claims.get("isAdmin");
                res.status(200);
                return "{\"message\":\"" + isAdmin + "\"}";
            } else {
                res.status(401);
                return "{\"message\":\"Not logged in\"}";
            }
        });
    }

    private void fillTags(EntityManager em, List<Tag> tags) {
        if (tags.size() < 5){
            Query query2 = em.createQuery("SELECT availableTag FROM AvailableTag a");
            @SuppressWarnings("unchecked") List<String> availableTags = (List<String>) query2.getResultList();
            for (String availableTag : availableTags) {
                boolean isContained = false;
                for (Tag tag : tags) {
                    if (tag.getName().equals(availableTag)) {
                        isContained = true;
                        break;
                    }
                }
                if (!isContained) tags.add(new Tag(availableTag));
                if (tags.size() == 5) break;
            }
        }
    }

    @Override
    public HandlerType getType() {
        return HandlerType.HOME;
    }

    private List<GameForResponse> gameForResponseList(List<Game> games){
        List<GameForResponse> gamesForResponse = new ArrayList<>();
        for (Game game : games) {
            gamesForResponse.add(new GameForResponse(game));
        }
        return gamesForResponse;
    }

    private  List<TagForResponse> tagForResponseList(List<Tag> tags){
        List<TagForResponse> tagForResponseList = new ArrayList<>();
        for (Tag tag : tags) {
            tagForResponseList.add(new TagForResponse(tag));
        }
        return tagForResponseList;
    }
}

