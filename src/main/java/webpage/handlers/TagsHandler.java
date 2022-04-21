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
import webpage.responseFormats.GameForResponse;
import webpage.responseFormats.SearchTagResponse;
import webpage.responseFormats.UserTagsResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static spark.Spark.*;
import static webpage.util.SecretKey.key;

public class TagsHandler extends AbstractHandler {
    public TagsHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle() {
        path("/tags", () -> {
            path("/available_tags", () -> {
                get("", (req, res) -> {
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        EntityManager em = emf.createEntityManager();
                        Query availableTagsQuery = em.createQuery("FROM AvailableTag");
                        try {
                            @SuppressWarnings("unchecked") List<AvailableTag> availableTags = availableTagsQuery.getResultList();
                            AvailableTagsResponse response = new AvailableTagsResponse(availableTags);
                            Gson gson = new Gson();
                            res.status(200);
                            return gson.toJson(response);
                        } catch (Throwable e) {
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
                        }
                    } else {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                });

                patch("/add", "application/json", (req, res) -> {
                    Gson gson = new Gson();
                    AvailableTagsRequest tagsRequest = gson.fromJson(req.body(), AvailableTagsRequest.class);
                    EntityManager em = emf.createEntityManager();
                    @SuppressWarnings("unchecked") List<AvailableTag> availableTags = em.createQuery("FROM AvailableTag")
                            .getResultList();
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        boolean isAdmin = (boolean) claims.get("isAdmin");
                        try {
                            em.close();
                            EntityManager em2 = emf.createEntityManager();
                            if (isAdmin) {
                                for (Tag tag : tagsRequest.getTags()) {
                                    try{
                                        em2.getTransaction().begin();
                                        boolean isContained = false;
                                        for (AvailableTag availableTag : availableTags) {
                                            if (availableTag.getAvailableTag().equals(tag.getName())) {
                                                isContained = true;
                                                break;
                                            }
                                        }
                                        if (!isContained) em2.merge(new AvailableTag(tag.getName()));
                                        em2.getTransaction().commit();
                                    }catch (Throwable r) {
                                        em2.getTransaction().rollback();
                                        res.status(500);
                                        return "{\"message\":\"Something went wrong, try again.\"}";
                                    }
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

                delete("/delete", "application/json", (req, res) -> {
                    Gson gson = new Gson();
                    AvailableTagsRequest tagsRequest = gson.fromJson(req.body(), AvailableTagsRequest.class);
                    EntityManager em = emf.createEntityManager();

                    @SuppressWarnings("unchecked") List<AvailableTag> availableTags = em.createQuery("FROM AvailableTag a")
                            .getResultList();
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        boolean isAdmin = (boolean) claims.get("isAdmin");
                        try {
                            em.close();
                            EntityManager em2 = emf.createEntityManager();
                            if (isAdmin) {
                                try{
                                    em2.getTransaction().begin();
                                    for (Tag tag : tagsRequest.getTags()) {
                                        for (AvailableTag availableTag : availableTags) {
                                            if (availableTag.getAvailableTag().equals(tag.getName())) {
                                                em2.remove(em2.contains(availableTag) ? availableTag : em2.merge(availableTag));
                                                break;
                                            }
                                        }
                                    }
                                    em2.getTransaction().commit();
                                    em2.getTransaction().begin();
                                    for (Tag tag : tagsRequest.getTags()) {
                                        @SuppressWarnings("unchecked") List<Game> games = (List<Game>) em2.createQuery("SELECT t.games FROM Tag t WHERE t.name = ?1")
                                                .setParameter(1, tag.getName())
                                                .getResultList();
                                        @SuppressWarnings("unchecked") List<User> users = (List<User>) em2.createQuery("SELECT t.users FROM Tag t WHERE t.name = ?1")
                                                .setParameter(1, tag.getName())
                                                .getResultList();
                                        for (Game game : games) {
                                            game.removeTag(tag);
                                            em2.merge(game);
                                        }
                                        for (User user : users) {
                                            user.removeTag(tag);
                                            em2.merge(user);
                                        }
                                    }
                                    em2.getTransaction().commit();
                                    res.status(200);
                                    return "{\"message\":\"Tags successfully removed.\"}";
                                }catch (Throwable r) {
                                    em2.getTransaction().rollback();
                                    res.status(500);
                                    return "{\"message\":\"Something went wrong, try again.\"}";
                                }
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
            });

            path("/users", () -> {
                get("", (req, res) -> {
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        Integer userId1 = (Integer) claims.get("id");
                        EntityManager em = emf.createEntityManager();
                        try {
                            @SuppressWarnings("unchecked") List<Tag> userlikedTags = em.createQuery("SELECT likedTags FROM User u WHERE u.id = :id")
                                    .setParameter("id", userId1)
                                    .getResultList();
                            UserTagsResponse response = new UserTagsResponse(userlikedTags);
                            Gson gson = new Gson();
                            res.status(200);
                            return gson.toJson(response);
                        } catch (Throwable e) {
                            res.status(500);
                            return "{\"message\":\"Something went wrong.\"}";
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
                        Integer userId1 = (Integer) claims.get("id");
                        EntityManager em = emf.createEntityManager();
                        try {
                            User user = (User) em.createQuery("FROM User u WHERE u.id = :id")
                                    .setParameter("id", userId1)
                                    .getSingleResult();
                            Set<Tag> userLikedTags = user.getLikedTags();
                            for (Tag tag : tagsRequest.getTags()) {
                                userLikedTags.remove(tag);
                            }
                            user.setLikedTags(userLikedTags);
                            try{
                            em.getTransaction().begin();
                            em.merge(user);
                            em.getTransaction().commit();
                            res.status(200);
                            return "{\"message\":\"Tags successfully removed\"}";
                            } catch (Throwable e) {
                                em.getTransaction().rollback();
                                res.status(500);
                                return "{\"message\":\"Something went wrong.\"}";
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
                    EntityManager em = emf.createEntityManager();
                    @SuppressWarnings("unchecked") List<String> availableTags = em.createQuery("SELECT availableTag FROM AvailableTag")
                            .getResultList();
                    for (Tag tag : tagsRequest.getTags()) {
                        if (!availableTags.contains(tag.getName())) return "{\"message\":\"Tag not available.\"}";
                    }
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        Integer userId1 = (Integer) claims.get("id");
                        try {
                            User user = (User) em.createQuery("FROM User u WHERE u.id = :id")
                                    .setParameter("id", userId1)
                                    .getSingleResult();
                            Set<Tag> userLikedTags = user.getLikedTags();
                            userLikedTags.addAll(tagsRequest.getTags());
                            user.setLikedTags(userLikedTags);
                            try{
                            em.getTransaction().begin();
                            em.merge(user);
                            em.getTransaction().commit();
                            res.status(200);
                            return "{\"message\":\"Tags successfully added.\"}";
                            } catch (Throwable e) {
                                em.getTransaction().rollback();
                                res.status(500);
                                return "{\"message\":\"Something went wrong.\"}";
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
                patch("/add", (req, res) -> {
                    Gson gson = new Gson();
                    UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
                    EntityManager em = emf.createEntityManager();
                    @SuppressWarnings("unchecked") List<String> availableTags = em.createQuery("SELECT availableTag FROM AvailableTag")
                            .getResultList();
                    for (Tag tag : tagsRequest.getTags()) {
                        if (!availableTags.contains(tag.getName())) return "{\"message\":\"Tag not available.\"}";
                    }
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        Integer userId1 = (Integer) claims.get("id");
                        try {
                            @SuppressWarnings("unchecked") List<Game> createdGames = em.createQuery("SELECT createdGames FROM User u WHERE u.id = :id")
                                    .setParameter("id", userId1)
                                    .getResultList();
                            String gameId = req.headers("gameId");
                            int gameid1 = Integer.parseInt(gameId);
                            for (Game game : createdGames) {
                                if (game.getId() == gameid1) {
                                    Set<Tag> gameTags = game.getTags();
                                    gameTags.addAll(tagsRequest.getTags());
                                    game.setTags(gameTags);
                                    try {
                                    em.getTransaction().begin();
                                    em.merge(game);
                                    em.getTransaction().commit();
                                    res.status(200);
                                    return "{\"message\":\"Tags added.\"}";
                                    } catch (Throwable e) {
                                        em.getTransaction().rollback();
                                        res.status(500);
                                        return "{\"message\":\"Something went wrong.\"}";
                                    }
                                }
                            }
                            res.status(401);
                            return "{\"message\":\"Unauthorized.\"}";
                        } catch (NoResultException e) {
                            res.status(400);
                            return "{\"message\":\"User not found.\"}";
                        }
                    } else {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
                });

                delete("/delete", (req, res) -> {
                    Gson gson = new Gson();
                    UserTagsRequest tagsRequest = gson.fromJson(req.body(), UserTagsRequest.class);
                    EntityManager em = emf.createEntityManager();
                    @SuppressWarnings("unchecked") List<String> availableTags = em.createQuery("SELECT availableTag FROM AvailableTag").getResultList();
                    for (Tag tag : tagsRequest.getTags()) {
                        if (!availableTags.contains(tag.getName())) return "{\"message\":\"Tag not available.\"}";
                    }
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        Claims claims = Jwts.parser()
                                .setSigningKey(key)
                                .parseClaimsJws(token).getBody();
                        String userId = (String) claims.get("id");
                        Integer userId1 = Integer.parseInt(userId);
                        try {
                            @SuppressWarnings("unchecked") List<Game> createdGames = em.createQuery("SELECT createdGames FROM User u WHERE u.id = :id")
                                    .setParameter("id", userId1)
                                    .getResultList();
                            String gameId = req.headers("gameId");
                            int gameid1 = Integer.parseInt(gameId);
                            for (Game game : createdGames) {
                                if (game.getId() == gameid1) {
                                    Set<Tag> gameTags = game.getTags();
                                    tagsRequest.getTags().forEach(gameTags::remove);
                                    game.setTags(gameTags);
                                    try {
                                        em.getTransaction().begin();
                                        em.merge(game);
                                        em.getTransaction().commit();
                                        res.status(200);
                                        return "{\"message\":\"Tags removed.\"}";
                                    } catch (Throwable e) {
                                        em.getTransaction().rollback();
                                        res.status(500);
                                        return "{\"message\":\"Something went wrong.\"}";
                                    }
                                }
                            }
                            res.status(401);
                            return "{\"message\":\"Unauthorized.\"}";
                        } catch (NoResultException e) {
                            res.status(400);
                            return "{\"message\":\"User not found.\"}";
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
                    EntityManager em = emf.createEntityManager();
                    @SuppressWarnings("unchecked") List<Game> games = em.createQuery("SELECT games FROM Tag t WHERE t.name = :search")
                            .setParameter("search", searchTag)
                            .getResultList();
                    List<GameForResponse> gamesForResponse = new ArrayList<>();
                    for (Game game : games) {
                        gamesForResponse.add(new GameForResponse(game));
                    }
                    Gson gson = new Gson();
                    return gson.toJson(new SearchTagResponse(gamesForResponse));
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
