package webpage.handlers;


import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Game;
import webpage.entity.Tag;
import webpage.entity.User;
import webpage.responseFormats.HomeResponse;
import webpage.responseFormats.SoftGameForResponse;
import webpage.responseFormats.TagForResponse;
import webpage.responseFormats.UserForResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
                Long userId = Long.valueOf((Integer) claims.get("id"));
                EntityManager em = emf.createEntityManager();
                User user = (User) em.createQuery("FROM User u WHERE u.id = ?1")
                        .setParameter(1, userId)
                        .getSingleResult();
                UserForResponse userForResponse = new UserForResponse(user);            // User ready
                @SuppressWarnings("unchecked") List<Tag> tags = (List<Tag>) em.createQuery("SELECT likedTags FROM User u WHERE u.id = ?1")
                        .setParameter(1, userId)
                        .getResultList();
                fillTags(em, tags);                                                    // Tags ready
                @SuppressWarnings("unchecked")List<Game> gamesTag1 = (List<Game>) em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1")
                        .setMaxResults(5)
                        .setParameter(1, tags.get(0).getName())
                        .getResultList();
                List<SoftGameForResponse> gamesForResponse1 = gameForResponseList(gamesTag1);
                @SuppressWarnings("unchecked")List<Game> gamesTag2 = (List<Game>) em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1")
                        .setMaxResults(5)
                        .setParameter(1, tags.get(1).getName())
                        .getResultList();
                List<SoftGameForResponse> gamesForResponse2 = gameForResponseList(gamesTag2);
                @SuppressWarnings("unchecked")List<Game> gamesTag3 = (List<Game>) em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1")
                        .setMaxResults(5)
                        .setParameter(1, tags.get(2).getName())
                        .getResultList();
                List<SoftGameForResponse> gamesForResponse3 = gameForResponseList(gamesTag3);
                @SuppressWarnings("unchecked")List<Game> gamesTag4 = (List<Game>) em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1")
                        .setMaxResults(5)
                        .setParameter(1, tags.get(3).getName())
                        .getResultList();
                List<SoftGameForResponse> gamesForResponse4 = gameForResponseList(gamesTag4);
                @SuppressWarnings("unchecked")List<Game> gamesTag5 = (List<Game>)em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1")
                        .setMaxResults(5)
                        .setParameter(1, tags.get(4).getName())
                        .getResultList();
                List<SoftGameForResponse> gamesForResponse5 = gameForResponseList(gamesTag5);
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
            @SuppressWarnings("unchecked") List<String> availableTags = (List<String>) em.createQuery("SELECT availableTag FROM AvailableTag a")
                    .getResultList();
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

    private List<SoftGameForResponse> gameForResponseList(List<Game> games){
        List<SoftGameForResponse> gamesForResponse = new ArrayList<>();
        for (Game game : games) {
            gamesForResponse.add(new SoftGameForResponse(game));
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

