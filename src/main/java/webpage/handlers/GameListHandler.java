package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.UserGame;
import webpage.requestFormats.GameListRequest;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static spark.Spark.*;
import static webpage.util.SecretKey.key;

public class GameListHandler extends AbstractHandler{
    public GameListHandler(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void handle() {
        path("/gamelist", () -> {
            get("/:userId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)){
                    EntityManager em = emf.createEntityManager();
                    try {
                        Long userId = (long) Integer.parseInt(req.params("userId"));
                        @SuppressWarnings("unchecked") List<UserGame> gameList = em.createQuery("FROM UserGame ug WHERE ug.userId = ?1")
                                .setParameter(1, userId)
                                .getResultList();
                        Gson gson = new Gson();
                        res.status(200);
                        return gson.toJson(gameList);
                    }finally {
                        em.close();
                    }

                }else{
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });

            patch("/:userId/add", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)){
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    int userId1 = (Integer) claims.get("id");
                    Gson gson = new Gson();
                    GameListRequest gameListRequest = gson.fromJson(req.body(), GameListRequest.class);
                    long userId =  Integer.parseInt(req.params("userId"));
                    if (userId == userId1){
                        EntityManager em = emf.createEntityManager();
                        @SuppressWarnings("unchecked") List<Long> gamesIds = em.createQuery("SELECT gameId FROM UserGame ug WHERE ug.userId = ?1")
                                .setParameter(1, (long) userId1)
                                .getResultList();
                        if (!gamesIds.contains(gameListRequest.getGameId())) {
                            UserGame userGame = new UserGame();
                            userGame.setUserId((long) userId1);
                            userGame.setStatus(gameListRequest.getStatus());
                            userGame.setScore(gameListRequest.getScore());
                            userGame.setGameId(gameListRequest.getGameId());
                            try {
                                em.getTransaction().begin();
                                em.merge(userGame);
                                em.getTransaction().commit();
                                res.status(200);
                                return "{\"message\":\"Game added to game list.\"}";
                            }catch (Throwable e) {
                                em.getTransaction().rollback();
                                res.status(500);
                                return "{\"message\":\"Something went wrong, try again.\"}";
                            }finally {
                                em.close();
                            }
                        } else{
                            res.status(409);
                            return "{\"message\":\"Game already in game list.\"}";
                        }
                    }else {
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });

            patch("/:userId/delete", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)){
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    int userId1 = (Integer) claims.get("id");
                    Gson gson = new Gson();
                    GameListRequest gameListRequest = gson.fromJson(req.body(), GameListRequest.class);
                    long userId =  Integer.parseInt(req.params("userId"));
                    if (userId == userId1){
                        EntityManager em = emf.createEntityManager();
                        @SuppressWarnings("unchecked") List<UserGame> game = em.createQuery("FROM UserGame ug WHERE ug.gameId = ?1 AND ug.userId = ?2")
                                .setParameter(1, gameListRequest.getGameId())
                                .setParameter(2, (long) userId1)
                                .getResultList();
                        if (!game.isEmpty()) {
                            UserGame userGame = game.get(0);
                            try {
                                em.getTransaction().begin();
                                em.remove(userGame);
                                em.getTransaction().commit();
                                res.status(200);
                                return "{\"message\":\"Game removed to game list.\"}";
                            }catch (Throwable e){
                                em.getTransaction().rollback();
                                res.status(500);
                                return "{\"message\":\"Something went wrong, try again.\"}";
                            }finally {
                                em.close();
                            }
                        } else{
                            res.status(409);
                            return "{\"message\":\"Game not in game list.\"}";
                        }
                    }else {
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });

            patch("/:userId/edit", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)){
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    int userId1 = (Integer) claims.get("id");
                    Gson gson = new Gson();
                    GameListRequest gameListRequest = gson.fromJson(req.body(), GameListRequest.class);
                    long userId =  Integer.parseInt(req.params("userId"));
                    if (userId == userId1){
                        EntityManager em = emf.createEntityManager();
                        @SuppressWarnings("unchecked") List<UserGame> game = em.createQuery("FROM UserGame ug WHERE ug.gameId = ?1 AND ug.userId = ?2")
                                .setParameter(1, gameListRequest.getGameId())
                                .setParameter(2, (long) userId1)
                                .getResultList();
                        if (!game.isEmpty()) {
                            UserGame userGame = game.get(0);
                            userGame.setStatus(gameListRequest.getStatus());
                            userGame.setScore(gameListRequest.getScore());
                            try {
                                em.getTransaction().begin();
                                em.merge(userGame);
                                em.getTransaction().commit();
                                res.status(200);
                                return "{\"message\":\"Game edited.\"}";
                            }catch (Throwable e) {
                                em.getTransaction().rollback();
                                res.status(500);
                                return "{\"message\":\"Something went wrong, try again.\"}";
                            }finally {
                                em.close();
                            }
                        } else{
                            res.status(409);
                            return "{\"message\":\"Game not in game list.\"}";
                        }
                    }else {
                        res.status(401);
                        return "{\"message\":\"Unauthorized.\"}";
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
        return HandlerType.GAMELIST;
    }
}
