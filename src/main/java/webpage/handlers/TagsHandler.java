package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.AvailableTag;
import webpage.entity.Tag;
import webpage.entity.User;
import webpage.requestFormats.AvailableTagsRequest;
import webpage.requestFormats.UserTagsRequest;
import webpage.responseFormats.UserTagsResponse;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

import static spark.Spark.*;
import static webpage.util.SecretKey.key;

public class TagsHandler extends AbstractHandler {
    public TagsHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle(String path) {
        enableCORS();
        patch(path + "/available_tags/add", "application/json", (req, res) -> {
            Gson gson = new Gson();
            AvailableTagsRequest tagsRequest = gson.fromJson(req.body(), AvailableTagsRequest.class);
            EntityManager em = emf.createEntityManager();
            Query query1 = em.createQuery("FROM AvailableTag");
            List<AvailableTag> availableTags = query1.getResultList();
            String token = req.headers("token");
            if (verifyJWT(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                String userId =  (String) claims.get("id");
                Integer userId1 = Integer.parseInt(userId);
                boolean isAdmin = (boolean) claims.get("isAdmin");
                Query query2 = em.createQuery("FROM User u WHERE u.id = :id");
                query2.setParameter("id", userId1);
                try {
                    query2.getSingleResult();
                    em.close();
                    EntityManager em2 = emf.createEntityManager();
                    if (isAdmin){
                        for (Tag tag : tagsRequest.getTags()) {
                            em2.getTransaction().begin();
                            boolean isContained = false;
                            for (AvailableTag availableTag : availableTags) {
                                if (availableTag.getAvailableTag().equals(tag.getName())) {
                                    isContained = true;
                                    break;
                                }
                            }
                            if (!isContained) em2.persist(new AvailableTag(tag.getName()));
                            em2.getTransaction().commit();
                        }
                        res.status(200);
                        return "{\"message\":\"Tags successfully added.\"}";
                    }
                    res.status(401);
                    return "{\"message\":\"Not authorized.\"}";
                } catch (NoResultException e) {
                    res.status(400);
                    return "{\"message\":\"User not found.\"}";
                }
            }
            res.status(401);
            return "{\"message\":\"Not logged in.\"}";
        });

        delete(path + "/available_tags/delete", "application/json", (req, res) -> {
            Gson gson = new Gson();
            AvailableTagsRequest tagsRequest = gson.fromJson(req.body(), AvailableTagsRequest.class);
            EntityManager em = emf.createEntityManager();
            Query query1 = em.createQuery("FROM AvailableTag");
            List<AvailableTag> availableTags = query1.getResultList();
            String token = req.headers("token");
            if (verifyJWT(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                String userId =  (String) claims.get("id");
                Integer userId1 = Integer.parseInt(userId);
                boolean isAdmin = (boolean) claims.get("isAdmin");
                Query query2 = em.createQuery("FROM User u WHERE u.id = :id");
                query2.setParameter("id", userId1);
                try {
                    query2.getSingleResult();
                    em.close();
                    EntityManager em2 = emf.createEntityManager();
                    if (isAdmin){
                        for (Tag tag : tagsRequest.getTags()) {
                            em2.getTransaction().begin();
                            for (AvailableTag availableTag : availableTags) {
                                if (availableTag.getAvailableTag().equals(tag.getName())) {
                                    em2.remove(em2.contains(availableTag) ? availableTag : em2.merge(availableTag));
                                    break;
                                }
                            }
                            em2.getTransaction().commit();
                        }
                        res.status(200);
                        return "{\"message\":\"Tags successfully removed.\"}";
                    }
                    res.status(401);
                    return "{\"message\":\"Not authorized.\"}";
                } catch (NoResultException e) {
                    res.status(400);
                    return "{\"message\":\"User not found.\"}";
                }
            }
            res.status(401);
            return "{\"message\":\"Not logged in.\"}";
        });

        delete(path + "/users/delete", "application/json", (req, res) -> {
            Gson gson = new Gson();
            UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
            String token = req.headers("token");
            if (verifyJWT(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                String userId =  (String) claims.get("id");
                Integer userId1 = Integer.parseInt(userId);
                EntityManager em = emf.createEntityManager();
                Query query = em.createQuery("FROM User u WHERE u.id = :id");
                query.setParameter("id", userId1);
                try {
                    User user = (User) query.getSingleResult();
                    Set<Tag> userLikedTags = user.getLikedTags();
                    for (Tag tag : tagsRequest.getTags()) {
                        userLikedTags.remove(tag);
                    }
                    user.setLikedTags(userLikedTags);
                    em.getTransaction().begin();
                    em.merge(user);
                    em.getTransaction().commit();
                    res.status(200);
                    return "{\"message\":\"Tags successfully removed\"}";
                } catch (NoResultException e) {
                    res.status(400);
                    return "{\"message\":\"User not found\"}";
                }
            }
            res.status(401);
            return "{\"message\":\"Not logged in\"}";
        });

        patch(path + "/users/add", "application/json", (req, res) -> {
            Gson gson = new Gson();
            UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
            EntityManager em = emf.createEntityManager();
            Query query1 = em.createQuery("FROM AvailableTag");
            List availableTags = query1.getResultList();
            for (Tag tag : tagsRequest.getTags()) {
                if (!availableTags.contains(tag.getName())) return "{\"message\":\"Tag not available.\"}";
            }
            String token = req.headers("token");
            if (verifyJWT(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                String userId =  (String) claims.get("id");
                Integer userId1 = Integer.parseInt(userId);
                Query query2 = em.createQuery("FROM User u WHERE u.id = :id");
                query2.setParameter("id", userId1);
                try {
                    User user = (User) query2.getSingleResult();
                    Set<Tag> userLikedTags = user.getLikedTags();
                    userLikedTags.addAll(tagsRequest.getTags());
                    user.setLikedTags(userLikedTags);
                    em.getTransaction().begin();
                    em.merge(user);
                    em.getTransaction().commit();
                    res.status(200);
                    return "{\"message\":\"Tags successfully added.\"}";
                } catch (NoResultException e) {
                    res.status(400);
                    return "{\"message\":\"User not found.\"}";
                }
            }
            res.status(401);
            return "{\"message\":\"Not logged in.\"}";
        });

        get(path + "/users",(req,res) -> {
            String token = req.headers("token");
            if (verifyJWT(token)){
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                String userId =  (String) claims.get("id");
                Integer userId1 = Integer.parseInt(userId);
                EntityManager em = emf.createEntityManager();
                Query availableTagsQuery = em.createQuery("FROM AvailableTag");
                Query userLikedTagsQuery = em.createQuery("SELECT likedTags FROM User u WHERE u.id = :id");
                userLikedTagsQuery.setParameter("id", userId1);
                try {
                    List<AvailableTag> availableTags = availableTagsQuery.getResultList();
                    List<Tag> userlikedTags = userLikedTagsQuery.getResultList();
                    UserTagsResponse response = new UserTagsResponse(availableTags, userlikedTags);
                    Gson gson = new Gson();
                    res.status(200);
                    return gson.toJson(response);
                }catch (Throwable e){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
            }else{
                res.status(401);
                return "{\"message\":\"Not logged in.\"}";
            }
        });

        get(path + "/available_tags", (req, res) -> {
            String token = req.headers("token");
            if (verifyJWT(token)){
                EntityManager em = emf.createEntityManager();
                Query availableTagsQuery = em.createQuery("FROM AvailableTag");
                try {
                    List<AvailableTag> availableTags = availableTagsQuery.getResultList();
                    UserTagsResponse response = new UserTagsResponse(availableTags, null);
                    Gson gson = new Gson();
                    res.status(200);
                    return gson.toJson(response);
                }catch (Throwable e){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }
            }else{
                res.status(401);
                return "{\"message\":\"Not logged in.\"}";
            }
        });
    }
}
