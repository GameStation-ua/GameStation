package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Actor;
import webpage.entity.Notification;
import webpage.entity.NotificationType;
import webpage.entity.User;
import webpage.requestFormats.FollowRequest;
import webpage.util.HandlerType;

import javax.persistence.NoResultException;
import java.util.Objects;

import static spark.Spark.patch;
import static spark.Spark.path;
import static webpage.handlers.NotificationHandler.sendNotification;
import static webpage.util.EntityManagers.close;
import static webpage.util.EntityManagers.currentEntityManager;
import static webpage.util.SecretKey.key;

public class FollowHandler extends AbstractHandler{

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
                   Actor actor;
                   User user;
                   try {
                       actor = (Actor) currentEntityManager().createQuery("FROM ACTOR a WHERE a.id = ?1")
                               .setParameter(1, Long.valueOf(req.params(":actorId")))
                               .getSingleResult();
                       user = (User) currentEntityManager().createQuery("FROM User u WHERE u.id = ?1")
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
                       try {
                           User user1 = (User) actor;
                           Notification notification = new Notification(NotificationType.USER_STARTED_FOLLOWING, user, actor, followRequest.getPath());
                           user1.addNotification(notification);
                           currentEntityManager().getTransaction().begin();
                           currentEntityManager().merge(user1);
                           currentEntityManager().getTransaction().commit();
                           sendNotification(userId, notification);
                       } catch (Throwable e) {
                           currentEntityManager().getTransaction().rollback();
                       }
                       try{
                           currentEntityManager().getTransaction().begin();
                           currentEntityManager().merge(user);
                           currentEntityManager().getTransaction().commit();
                           res.status(200);
                           return "{\"message\":\"OK.\"}";
                       } catch(Throwable e){
                           currentEntityManager().getTransaction().rollback();
                           res.status(500);
                           return "{\"message\":\"Something went wrong.\"}";
                       }finally{
                           close();
                       }
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
                       Actor actor;
                       User user;
                       try {
                           actor = (Actor) currentEntityManager().createQuery("FROM ACTOR a WHERE a.id = ?1")
                                   .setParameter(1, Long.valueOf(req.params(":actorId")))
                                   .getSingleResult();
                           user = (User) currentEntityManager().createQuery("FROM User u WHERE u.id = ?1")
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
                           try {
                               currentEntityManager().getTransaction().begin();
                               currentEntityManager().merge(user);
                               currentEntityManager().getTransaction().commit();
                               res.status(200);
                               return "{\"message\":\"OK.\"}";
                           } catch (Throwable e) {
                               currentEntityManager().getTransaction().rollback();
                               res.status(500);
                               return "{\"message\":\"Something went wrong.\"}";
                           }finally {
                               close();
                           }
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

    @Override
    public HandlerType getType() {
        return HandlerType.FOLLOW;
    }
}
