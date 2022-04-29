package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.AvailableTag;
import webpage.entity.Game;
import webpage.entity.Tag;
import webpage.entity.User;
import webpage.requestFormats.AvailableTagsRequest;
import webpage.requestFormats.UserTagsRequest;
import webpage.responseFormats.AvailableTagsResponse;
import webpage.responseFormats.GameResponse;
import webpage.responseFormats.SearchTagResponse;
import webpage.responseFormats.UserTagsResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;
import static webpage.util.EntityManagers.close;
import static webpage.util.EntityManagers.currentEntityManager;
import static webpage.util.SecretKey.key;

public class TagsHandler extends AbstractHandler {

    public void handle() {
        path("/tags", () -> {
            path("/available_tags", () -> {
                get("", (req, res) -> {
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        
                        Query availableTagsQuery = currentEntityManager().createQuery("FROM AvailableTag");
                        try {
                            @SuppressWarnings("unchecked") List<AvailableTag> availableTags = availableTagsQuery.getResultList();
                            AvailableTagsResponse response = new AvailableTagsResponse(availableTags);
                            Gson gson = new Gson();
                            res.status(200);
                            return gson.toJson(response);
                        } catch (Throwable e) {
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
                        }finally {
                            close();
                        }
                    } else {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                });

                patch("/add", "application/json", (req, res) -> {
                    Gson gson = new Gson();
                    AvailableTagsRequest tagsRequest = gson.fromJson(req.body(), AvailableTagsRequest.class);
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        boolean isAdmin = (boolean) claims.get("isAdmin");
                        if (isAdmin) {
                            
                            try {
                                @SuppressWarnings("unchecked") List<AvailableTag> availableTags = currentEntityManager().createQuery("FROM AvailableTag")
                                        .getResultList();
                                    try {
                                        currentEntityManager().getTransaction().begin();
                                        for (AvailableTag tagsRequestTag : tagsRequest.getTags()) {
                                            if (!availableTags.contains(tagsRequestTag)) {
                                                currentEntityManager().merge(tagsRequestTag);
                                            }
                                        }
                                        currentEntityManager().getTransaction().commit();
                                    } catch (Throwable r) {
                                        currentEntityManager().getTransaction().rollback();
                                        res.status(500);
                                        return "{\"message\":\"Something went wrong, try again.\"}";
                                    }
                                res.status(200);
                                return "{\"message\":\"Tags successfully added.\"}";
                            } catch (Throwable e) {
                                res.status(500);
                                return "{\"message\":\"Something went wrong, try again.\"}";
                            } finally {
                                close();
                            }
                        }
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
                    }
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                });

                delete("/delete", "application/json", (req, res) -> {
                    Gson gson = new Gson();
                    AvailableTagsRequest tagsRequest = gson.fromJson(req.body(), AvailableTagsRequest.class);
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        boolean isAdmin = (boolean) claims.get("isAdmin");
                        if (isAdmin) {
                            
                            try {
                                @SuppressWarnings("unchecked") List<AvailableTag> availableTags = currentEntityManager().createQuery("FROM AvailableTag")
                                        .getResultList();
                                try {
                                    currentEntityManager().getTransaction().begin();
                                    for (AvailableTag availableTag : availableTags) {
                                        for (AvailableTag tag : tagsRequest.getTags()) {
                                            if (tag.equals(availableTag)) currentEntityManager().remove(availableTag);
                                        }
                                    }
                                    currentEntityManager().getTransaction().commit();
                                } catch (Throwable r) {
                                    currentEntityManager().getTransaction().rollback();
                                    res.status(500);
                                    return "{\"message\":\"Something went wrong, try again.\"}";
                                }
                                res.status(200);
                                return "{\"message\":\"Tags successfully removed.\"}";
                            } catch (Throwable e) {
                                res.status(500);
                                return "{\"message\":\"Something went wrong, try again.\"}";
                            } finally {
                                close();
                            }
                        }
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
                    }
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                });
            });

            path("/users", () -> {
                get("", (req, res) -> {
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        Long userId = Long.valueOf((Integer)claims.get("id"));
                        
                        try {
                            User user = (User) currentEntityManager().createQuery("FROM User u WHERE u.id = :id")
                                    .setParameter("id", userId)
                                    .getSingleResult();
                            UserTagsResponse response = new UserTagsResponse(user.getLikedTags());
                            Gson gson = new Gson();
                            res.status(200);
                            return gson.toJson(response);
                        } catch (NoResultException e) {
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
                        }finally {
                            close();
                        }
                    } else {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                });

                delete("/delete", "application/json", (req, res) -> {
                    Gson gson = new Gson();
                    UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        Long userId1 = Long.valueOf((Integer) claims.get("id"));
                        
                        try {
                            User user = (User) currentEntityManager().createQuery("FROM User u WHERE u.id = :id")
                                    .setParameter("id", userId1)
                                    .getSingleResult();
                            tagsRequest.getTags().forEach(user.getLikedTags()::remove);
                            try{
                            currentEntityManager().getTransaction().begin();
                            currentEntityManager().merge(user);
                            currentEntityManager().getTransaction().commit();
                            res.status(200);
                            return "{\"message\":\"Tags successfully removed\"}";
                            } catch (Throwable e) {
                                currentEntityManager().getTransaction().rollback();
                                res.status(500);
                                return "{\"message\":\"Something went wrong.\"}";
                            }finally {
                                close();
                            }
                        } catch (NoResultException e) {
                            res.status(400);
                            return "{\"message\":\"User not found\"}";
                        }
                    }
                    res.status(401);
                    return "{\"message\":\"Not logged in\"}";
                });

                patch("/add", "application/json", (req, res) -> {
                    Gson gson = new Gson();
                    UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
                    
                    @SuppressWarnings("unchecked") List<String> availableTags = currentEntityManager().createQuery("SELECT availableTag FROM AvailableTag")
                            .getResultList();
                    for (Tag tag : tagsRequest.getTags()) {
                        if (!availableTags.contains(tag.getName())) return "{\"message\":\"Tag not available.\"}";
                    }
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        Long userId1 = Long.valueOf((Integer)claims.get("id"));
                        try {
                            User user = (User) currentEntityManager().createQuery("FROM User u WHERE u.id = :id")
                                    .setParameter("id", userId1)
                                    .getSingleResult();
                            user.getLikedTags().addAll(tagsRequest.getTags());
                            try{
                            currentEntityManager().getTransaction().begin();
                            currentEntityManager().merge(user);
                            currentEntityManager().getTransaction().commit();
                            res.status(200);
                            return "{\"message\":\"Tags successfully added.\"}";
                            } catch (Throwable e) {
                                currentEntityManager().getTransaction().rollback();
                                res.status(500);
                                return "{\"message\":\"Something went wrong.\"}";
                            }finally {
                                close();
                            }
                        } catch (NoResultException e) {
                            res.status(400);
                            return "{\"message\":\"User not found.\"}";
                        }
                    }
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                });
            });

            path("/games", () -> {
                patch("/add/:gameId", (req, res) -> {
                    Gson gson = new Gson();
                    UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
                    
                    @SuppressWarnings("unchecked") List<String> availableTags = currentEntityManager().createQuery("SELECT availableTag FROM AvailableTag")
                            .getResultList();
                    for (Tag tag : tagsRequest.getTags()) {
                        if (!availableTags.contains(tag.getName())) return "{\"message\":\"Tag not available.\"}";
                    }
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        long userId =  Long.valueOf((Integer) claims.get("id"));
                        Game game;
                        try {
                            game = (Game) currentEntityManager().createQuery("FROM Game g WHERE g.id = ?1")
                                    .setParameter(1, Long.valueOf(req.params(":gameId")))
                                    .getSingleResult();
                            if (game.getCreatorId() != userId){
                                res.status(401);
                                return "{\"message\":\"Unauthorized.\"}";
                            }
                            for (Tag tag : tagsRequest.getTags()) {
                                game.addTag(tag);
                            }
                        }catch (NoResultException e) {
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
                        }
                        try{
                            currentEntityManager().getTransaction().begin();
                            currentEntityManager().merge(game);
                            currentEntityManager().getTransaction().commit();
                            res.status(200);
                            return "{\"message\":\"Tags added.\"}";
                        }catch (Throwable e) {
                            currentEntityManager().getTransaction().rollback();
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
                        }finally {
                            close();
                        }
                    } else {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                });

                delete("/delete/:gameId", (req, res) -> {
                    Gson gson = new Gson();
                    UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
                    
                    @SuppressWarnings("unchecked") List<String> availableTags = currentEntityManager().createQuery("SELECT availableTag FROM AvailableTag").getResultList();
                    for (Tag tag : tagsRequest.getTags()) {
                        if (!availableTags.contains(tag.getName())) return "{\"message\":\"Tag not available.\"}";
                    }
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        long userId =  Long.valueOf((Integer) claims.get("id"));
                        Game game;
                        try {
                            game = (Game) currentEntityManager().createQuery("FROM Game g WHERE g.id = ?1")
                                    .setParameter(1, Long.valueOf(req.params(":gameId")))
                                    .getSingleResult();
                            if (game.getCreatorId() != userId){
                                res.status(401);
                                return "{\"message\":\"Unauthorized.\"}";
                            }
                            for (Tag tag : tagsRequest.getTags()) {
                                game.removeTag(tag);
                            }
                        }catch (NoResultException e) {
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
                        }
                        try{
                            currentEntityManager().getTransaction().begin();
                            currentEntityManager().merge(game);
                            currentEntityManager().getTransaction().commit();
                            res.status(200);
                            return "{\"message\":\"Tags deleted.\"}";
                            }catch (Throwable e) {
                            currentEntityManager().getTransaction().rollback();
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
                        }finally {
                            close();
                        }
                    } else {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                });
            });

            get("/search/:searchTag", (req,res) -> {
                String searchTag = req.params(":searchTag");
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    
                    try {
                        @SuppressWarnings("unchecked") List<Game> games = currentEntityManager().createQuery("SELECT games FROM Tag t WHERE t.name = :search")
                                .setParameter("search", searchTag)
                                .getResultList();
                        List<GameResponse> gamesForResponse = new ArrayList<>();
                        for (Game game : games) {
                            gamesForResponse.add(new GameResponse(game));
                        }
                        Gson gson = new Gson();
                        return gson.toJson(new SearchTagResponse(gamesForResponse));
                    }catch (Throwable e){
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }finally {
                        close();
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
        return HandlerType.TAGS;
    }
}
