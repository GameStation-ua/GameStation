package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.*;
import webpage.entity.Thread;
import webpage.requestFormats.CommentRequest;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import static spark.Spark.path;
import static spark.Spark.post;
import static webpage.util.SecretKey.key;

public class CommentHandler extends AbstractHandler{
    public CommentHandler(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void handle() {
        path("/comment", () -> {
            post("/game/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Gson gson = new Gson();
                    CommentRequest commentRequest = gson.fromJson(req.body(), CommentRequest.class);
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    Long gameId = Long.valueOf(req.params(":gameId"));
                    EntityManager em = emf.createEntityManager();
                    Actor game;
                    Actor user;
                    try {
                        game = (Actor) em.createQuery("FROM Game g WHERE g.id = ?1")
                                .setParameter(1, gameId)
                                .getSingleResult();
                        user = (User) em.createQuery("FROM User u WHERE u.id = ?1")
                                .setParameter(1, userId)
                                .getSingleResult();
                    } catch (NoResultException e) {
                        res.status(400);
                        return "{\"message\":\"Game not found.\"}";
                    }
                    Comment comment = new Comment(userId, gameId, commentRequest.getContent());
                    game.addComment(comment);
                    Notification notification = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user, game, commentRequest.getPath());
                    for (User follower : user.getFollowers()) {
                        follower.addNotification(notification);
                    }
                    try{
                        em.getTransaction().begin();
                        em.merge(user);
                        em.merge(game);
                        em.getTransaction().commit();
                        res.status(200);
                        return "{\"message\":\"OK.\"}";
                    }catch (Throwable e){
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
            });

            post("/thread/:threadId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Gson gson = new Gson();
                    CommentRequest commentRequest = gson.fromJson(req.body(), CommentRequest.class);
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    Long threadId = Long.valueOf(req.params(":threadId"));
                    EntityManager em = emf.createEntityManager();
                    Thread thread;
                    User user;
                    User creator;
                    try {
                        thread = (Thread) em.createQuery("FROM Thread t WHERE t.id = ?1")
                                .setParameter(1, threadId)
                                .getSingleResult();
                        creator = (User) em.createQuery("FROM User u WHERE u.id = ?1")
                                .setParameter(1, thread.getCreatorId())
                                .getSingleResult();
                        user = (User) em.createQuery("FROM User u WHERE u.id = ?1")
                                .setParameter(1, userId)
                                .getSingleResult();
                    } catch (NoResultException e) {
                        res.status(400);
                        return "{\"message\":\"Game not found.\"}";
                    }
                    Comment comment = new Comment(userId, threadId, commentRequest.getContent());
                    thread.addComment(comment);
                    Notification notification = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user, thread, commentRequest.getPath());
                    for (User follower : user.getFollowers()) {
                        follower.addNotification(notification);
                    }
                    creator.addNotification(new Notification(NotificationType.USER_COMMENTED_ON_USER_THREAD, user, thread, commentRequest.getPath()));
                    Notification notification1 = new Notification(NotificationType.USER_COMMENTED_ON_FOLLOWED_THREAD, user, thread, commentRequest.getPath());
                    for (User follower : thread.getFollowers()) {
                        follower.addNotification(notification1);
                    }
                    try{
                        em.getTransaction().begin();
                        em.merge(creator);
                        em.merge(user);
                        em.merge(thread);
                        em.getTransaction().commit();
                        res.status(200);
                        return "{\"message\":\"OK.\"}";
                    }catch (Throwable e){
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
            });

            post("/user/:targetUserId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Gson gson = new Gson();
                    CommentRequest commentRequest = gson.fromJson(req.body(), CommentRequest.class);
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    Long targetUserId = Long.valueOf(req.params(":targetUserId"));
                    EntityManager em = emf.createEntityManager();
                    User targetUser;
                    User user;
                    try {
                        targetUser = (User) em.createQuery("FROM User g WHERE g.id = ?1")
                                .setParameter(1, targetUserId)
                                .getSingleResult();
                        user = (User) em.createQuery("FROM User u WHERE u.id = ?1")
                                .setParameter(1, userId)
                                .getSingleResult();
                    } catch (NoResultException e) {
                        res.status(400);
                        return "{\"message\":\"Game not found.\"}";
                    }
                    Comment comment = new Comment(userId, targetUserId, commentRequest.getContent());
                    targetUser.addComment(comment);
                    Notification notification = new Notification(NotificationType.FOLLOWED_USER_COMMENTS, user, targetUser, commentRequest.getPath());
                    for (User follower : user.getFollowers()) {
                        follower.addNotification(notification);
                    }
                    targetUser.addNotification(new Notification(NotificationType.USER_COMMENTED_ON_PROFILE, user, targetUser, commentRequest.getPath()));
                    try{
                        em.getTransaction().begin();
                        em.merge(user);
                        em.merge(targetUser);
                        em.getTransaction().commit();
                        res.status(200);
                        return "{\"message\":\"OK.\"}";
                    }catch (Throwable e){
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.COMMENT;
    }
}
