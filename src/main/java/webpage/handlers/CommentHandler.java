package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Thread;
import webpage.entity.*;
import webpage.requestFormats.CommentRequest;
import webpage.responseFormats.CommentListResponse;
import webpage.responseFormats.CommentResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;
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
                    }finally {
                        em.close();
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
                    }finally {
                        em.close();
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
                    }finally {
                        em.close();
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
                    }finally {
                        em.close();
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
            });

            get("/*/*", (req, res) ->{
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    String[] request = req.splat();
                    Long actorId = Long.valueOf(request[0]);
                    int pageNumber = Integer.parseInt(request[1]);
                    EntityManager em = emf.createEntityManager();
                    try {
                        @SuppressWarnings("unchecked") List<Comment> comments = em.createQuery("SELECT comments FROM ACTOR a WHERE a.id = ?1")
                                .setParameter(1, actorId)
                                .setFirstResult(pageNumber * 10 - 10)
                                .setMaxResults(10)
                                .getResultList();
                        List<CommentResponse> commentResponseList = new ArrayList<>();
                        try {
                            for (Comment comment : comments) {
                                Integer vote;
                                try{
                                    vote = (Integer) em.createQuery("SELECT vote FROM UserComment uc WHERE uc.userId = ?1 AND uc.commentId = ?2")
                                            .setParameter(1, userId)
                                            .setParameter(2, comment.getId())
                                            .getSingleResult();
                                }catch (NoResultException e){
                                    vote = 0;
                                }
                                String nickname = (String) em.createQuery("SELECT name FROM User u WHERE u.id = ?1")
                                        .setParameter(1, comment.getUserId())
                                        .getSingleResult();
                                commentResponseList.add(new CommentResponse(comment, nickname, vote));
                            }
                            Gson gson = new Gson();
                            return gson.toJson(new CommentListResponse(commentResponseList));
                        }catch (Throwable e){
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
                        }
                    } catch (NoResultException e) {
                        res.status(400);
                        return "{\"message\":\"Wrong id.\"}";
                    }finally {
                        em.close();
                    }
                }else{
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });

            post("/upvote/:commentId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    Long commentId =  Long.valueOf(req.params(":commentId"));
                    EntityManager em = emf.createEntityManager();
                    UserComment userComment = null;
                    Comment comment;
                    try{
                        userComment = (UserComment) em.createQuery("FROM UserComment WHERE commentId = ?1 AND userId = ?2")
                                .setParameter(1, commentId)
                                .setParameter(2, userId)
                                .getSingleResult();
                    }catch (Throwable ignored){}
                    try {
                        comment = (Comment) em.createQuery("FROM Comment c WHERE c.id = ?1")
                                .setParameter(1, commentId)
                                .getSingleResult();
                        if (comment == null){
                            res.status(400);
                            return "{\"message\":\"Bad request.\"}";
                        }
                        if (userComment == null) {
                            userComment = new UserComment(userId, commentId, 1);
                            comment.setVotes(comment.getVotes() + 1);
                        }else{
                            Integer votes = userComment.getVote();
                            switch (votes){
                                case 1: break;
                                case 0: userComment.setVote(1); comment.setVotes(comment.getVotes() + 1); break;
                                case -1: userComment.setVote(1); comment.setVotes(comment.getVotes() + 2); break;
                            }
                        }
                        em.getTransaction().begin();
                        em.merge(comment);
                        em.merge(userComment);
                        em.getTransaction().commit();
                        return "{\"message\":\"OK.\"}";
                    }catch(Throwable e){
                        em.getTransaction().rollback();
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }finally {
                        em.close();
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });

            post("/downvote/:commentId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    Long commentId =  Long.valueOf(req.params(":commentId"));
                    EntityManager em = emf.createEntityManager();
                    UserComment userComment = null;
                    Comment comment;
                    try{
                        userComment = (UserComment) em.createQuery("FROM UserComment WHERE commentId = ?1 AND userId = ?2")
                                .setParameter(1, commentId)
                                .setParameter(2, userId)
                                .getSingleResult();
                    }catch (Throwable ignored){}
                    try {
                        comment = (Comment) em.createQuery("FROM Comment c WHERE c.id = ?1")
                                .setParameter(1, commentId)
                                .getSingleResult();
                        if (comment == null){
                            res.status(400);
                            return "{\"message\":\"Bad request.\"}";
                        }
                        if (userComment == null) {
                            userComment = new UserComment(userId, commentId, -1);
                            comment.setVotes(comment.getVotes() - 1);
                        }else{
                            Integer votes = userComment.getVote();
                            switch (votes){
                                case 1: userComment.setVote(-1); comment.setVotes(comment.getVotes() - 2);break;
                                case 0: userComment.setVote(-1); comment.setVotes(comment.getVotes() - 1); break;
                                case -1: break;
                            }
                        }
                        em.getTransaction().begin();
                        em.merge(comment);
                        em.merge(userComment);
                        em.getTransaction().commit();
                        return "{\"message\":\"OK.\"}";
                    }catch(Throwable e){
                        em.getTransaction().rollback();
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }finally {
                        em.close();
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });

            post("/neutralvote/:commentId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    Long commentId =  Long.valueOf(req.params(":commentId"));
                    EntityManager em = emf.createEntityManager();
                    UserComment userComment = null;
                    Comment comment;
                    try{
                        userComment = (UserComment) em.createQuery("FROM UserComment WHERE commentId = ?1 AND userId = ?2")
                                .setParameter(1, commentId)
                                .setParameter(2, userId)
                                .getSingleResult();
                    }catch (Throwable ignored){}
                    try {
                        comment = (Comment) em.createQuery("FROM Comment c WHERE c.id = ?1")
                                .setParameter(1, commentId)
                                .getSingleResult();
                        if (comment == null){
                            res.status(400);
                            return "{\"message\":\"Bad request.\"}";
                        }
                        if (userComment == null) {
                            userComment = new UserComment(userId, commentId, 0);
                        }else{
                            Integer votes = userComment.getVote();
                            switch (votes){
                                case 1: userComment.setVote(0); comment.setVotes(comment.getVotes() - 1); break;
                                case 0: break;
                                case -1: userComment.setVote(0); comment.setVotes(comment.getVotes() + 1); break;
                            }
                        }
                        em.getTransaction().begin();
                        em.merge(comment);
                        em.merge(userComment);
                        em.getTransaction().commit();
                        return "{\"message\":\"OK.\"}";
                    }catch(Throwable e){
                        em.getTransaction().rollback();
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }finally {
                        em.close();
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
        return HandlerType.COMMENT;
    }
}
