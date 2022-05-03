package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.model.Actor;
import webpage.model.User;
import webpage.requestFormats.FollowRequest;
import webpage.util.HandlerType;

import java.util.Optional;

import static spark.Spark.patch;
import static spark.Spark.path;
import static webpage.entity.Actors.checkIfFollows;
import static webpage.entity.Actors.findActorById;
import static webpage.entity.Persister.merge;
import static webpage.entity.Users.*;
import static webpage.util.SecretKey.key;

public class FollowHandler extends AbstractHandler{

    @Override
    public void handle() {
        path("/follow", () -> {
           patch("/add/:actorId", (req, res) -> {
               String token = req.headers("token");
               if (!verifyJWT(token)) {
                   res.status(401);
                   return "{\"message\":\"Not logged in.\"}";
               }
               FollowRequest followRequest = new Gson().fromJson(req.body(), FollowRequest.class);
               Long userId = getIdByToken(token);

               Optional<Actor> actor = findActorById(Long.valueOf(req.params(":actorId")));
               Optional<User> user = findUserById(userId);
               if (actor.isEmpty() || user.isEmpty()){
                       res.status(400);
                       return "{\"message\":\"Something went wrong.\"}";
               }
               if (checkIfFollows(userId, actor.get())) {
                   res.status(400);
                   return "{\"message\":\"Already follows.\"}";
               }
               user.get().addFollowedActor(actor.get());
               notifyIfUser(followRequest, userId, actor.get(), user.get());
               try{
                   merge(user.get());
                   res.status(200);
                   return "{\"message\":\"OK.\"}";
               } catch(Throwable e){
                   res.status(500);
                   return "{\"message\":\"Something went wrong.\"}";
               }
           });

           patch("/delete/:actorId", (req, res) -> {
               String token = req.headers("token");
               if (!verifyJWT(token)) {
                   res.status(401);
                   return "{\"message\":\"Not logged in.\"}";
               }
               Claims claims = Jwts.parser()
                       .setSigningKey(key)
                       .parseClaimsJws(token).getBody();
               Long userId = Long.valueOf((Integer) claims.get("id"));

               Optional<Actor> actor = findActorById(Long.valueOf(req.params(":actorId")));
               Optional<User> user = findUserById(userId);

               if (actor.isEmpty() || user.isEmpty()){
                   res.status(400);
                   return "{\"message\":\"Bad request.\"}";
               }


               if (!checkIfFollows(userId, actor.get())) {
                   res.status(400);
                   return "{\"message\":\"Doesn't follow.\"}";
               }
               user.get().removeFollowedActor(actor.get());
               try {
                   merge(user.get());
                   res.status(200);
                   return "{\"message\":\"OK.\"}";
               } catch (Throwable e) {
                   res.status(500);
                   return "{\"message\":\"Something went wrong.\"}";
               }
           });
        });

    }



    @Override
    public HandlerType getType() {
        return HandlerType.FOLLOW;
    }
}
