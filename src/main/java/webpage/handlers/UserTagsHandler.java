package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Tag;
import webpage.entity.User;
import webpage.requestFormats.UserTagsRequest;

import javax.persistence.*;

import static webpage.util.SecretKey.key;

import java.util.List;
import java.util.Set;

import static spark.Spark.post;

public class UserTagsHandler extends AbstractHandler {
    public UserTagsHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle(String path) {
        post(path + "/delete", "application/json", (req, res) -> {
            Gson gson = new Gson();
            UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
            if (verifyJWT(tagsRequest.getToken())) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(tagsRequest.getToken()).getBody();
                String userId =  (String) claims.get("id");
                Integer userId1 = Integer.parseInt(userId);
                EntityManager em = emf.createEntityManager();
                Query query = em.createQuery("FROM User u WHERE u.id = :id");
                query.setParameter("id", userId1);
                try {
                    User user = (User) query.getSingleResult();
                    Set<Tag> userLikedTags = user.getLikedtags();
                    for (Tag tag : tagsRequest.getTags()) {
                        userLikedTags.remove(tag);
                    }
                    user.setLikedtags(userLikedTags);
                    em.getTransaction().begin();
                    em.merge(user);
                    em.getTransaction().commit();
                    return "{\"message\":\"Tags successfully removed\"}";
                } catch (NoResultException e) {
                    res.status(400);
                    return "{\"message\":\"User not found\"}";
                }
            }
            res.status(401);
            return "{\"message\":\"Not logged in\"}";
        });

        post(path + "/add", "application/json", (req, res) -> {
            Gson gson = new Gson();
            UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
            EntityManager em = emf.createEntityManager();
            Query query1 = em.createQuery("FROM AvailableTag");
            List availableTags = query1.getResultList();
            for (Tag tag : tagsRequest.getTags()) {
                if (!availableTags.contains(tag.getName())) return "{\"message\":\"Tag not available.\"}";
            }
            if (verifyJWT(tagsRequest.getToken())) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(tagsRequest.getToken()).getBody();
                String userId =  (String) claims.get("id");
                Integer userId1 = Integer.parseInt(userId);
                Query query2 = em.createQuery("FROM User u WHERE u.id = :id");
                query2.setParameter("id", userId1);
                try {
                    User user = (User) query2.getSingleResult();
                    Set<Tag> userLikedTags = user.getLikedtags();
                    userLikedTags.addAll(tagsRequest.getTags());
                    user.setLikedtags(userLikedTags);
                    em.getTransaction().begin();
                    em.merge(user);
                    em.getTransaction().commit();
                    return "{\"message\":\"Tags successfully added.\"}";
                } catch (NoResultException e) {
                    res.status(400);
                    return "{\"message\":\"User not found.\"}";
                }
            }
            res.status(401);
            return "{\"message\":\"Not logged in.\"}";
        });
    }
}
