package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import spark.Response;
import webpage.entity.Actor;
import webpage.entity.Notification;
import webpage.entity.NotificationType;
import webpage.entity.User;
import webpage.requestFormats.FollowRequest;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.Objects;

import static spark.Spark.patch;
import static spark.Spark.path;
import static webpage.util.SecretKey.key;

public class FollowHandler extends AbstractHandler{
    public FollowHandler(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void handle() {
        path("/follow", () -> {
           patch("/add/:actorId", (req, res) -> {
               String token = req.headers("token");
               if (verifyJWT(token)) {
                   Claims claims = Jwts.parser()
                           .setSigningKey(key)
                           .parseClaimsJws(token).getBody();
                   Gson gson = new Gson();
                   FollowRequest followRequest = gson.fromJson(req.body(), FollowRequest.class);
                   Long userId = Long.valueOf((Integer) claims.get("id"));
                   EntityManager em = emf.createEntityManager();
                   User actor;
                   User user;
                   try {
                       actor = (User) em.createQuery("FROM ACTOR a WHERE a.id = ?1")
                               .setParameter(1, Long.valueOf(req.params(":actorId")))
                               .getSingleResult();
                       user = (User) em.createQuery("FROM User u WHERE u.id = ?1")
                               .setParameter(1, userId)
                               .getSingleResult();
                   } catch (NoResultException e) {
                       res.status(400);
                       return "{\"message\":\"Bad request.\"}";
                   }
                   boolean alreadyFollows = false;
                   for (User follower : actor.getFollowers()) {
                       if (Objects.equals(follower.getId(), userId)){
                           alreadyFollows = true;
                           break;
                       }
                   }
                   if (!alreadyFollows) {
                       user.addFollowedActor(actor);
                       actor.addNotification(new Notification(NotificationType.USER_STARTED_FOLLOWING, user, actor, followRequest.getPath()));
                       return persist(res, em, user);
                   }else {
                       res.status(400);
                       return "{\"message\":\"Already follows.\"}";
                   }
               }else {
                   res.status(401);
                   return "{\"message\":\"Not logged in.\"}";
               }
           });
               patch("/delete/:actorId", (req, res) -> {
                   String token = req.headers("token");
                   if (verifyJWT(token)) {
                       Claims claims = Jwts.parser()
                               .setSigningKey(key)
                               .parseClaimsJws(token).getBody();
                       Long userId = Long.valueOf((Integer) claims.get("id"));
                       EntityManager em = emf.createEntityManager();
                       Actor actor;
                       User user;
                       try {
                           actor = (Actor) em.createQuery("FROM ACTOR a WHERE a.id = ?1")
                                   .setParameter(1, Long.valueOf(req.params(":actorId")))
                                   .getSingleResult();
                           user = (User) em.createQuery("FROM User u WHERE u.id = ?1")
                                   .setParameter(1, userId)
                                   .getSingleResult();
                       } catch (NoResultException e) {
                           res.status(400);
                           return "{\"message\":\"Bad request.\"}";
                       }
                       boolean Follows = false;
                       for (User follower : actor.getFollowers()) {
                           if (Objects.equals(follower.getId(), userId)){
                               Follows = true;
                               break;
                           }
                       }
                       if (Follows) {
                           user.removeFollowedActor(actor);
                           return persist(res, em, user);
                       }else {
                           res.status(400);
                           return "{\"message\":\"Doesn't follow.\"}";
                       }
                   }else {
                       res.status(401);
                       return "{\"message\":\"Not logged in.\"}";
                   }
           });
        });

    }

    private String persist(Response res, EntityManager em, User user) {
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            res.status(200);
            return "{\"message\":\"OK.\"}";
        } catch (Throwable e) {
            em.getTransaction().rollback();
            res.status(500);
            return "{\"message\":\"Something went wrong.\"}";
        }finally {
            em.close();
        }
    }

    @Override
    public HandlerType getType() {
        return HandlerType.FOLLOW;
    }
}
