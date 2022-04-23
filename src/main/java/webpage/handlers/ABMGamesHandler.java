package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.*;
import webpage.requestFormats.CommentRequest;
import webpage.responseFormats.HardGameForResponse;
import webpage.responseFormats.TagForResponse;
import webpage.responseFormats.UserForResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;
import static webpage.util.SecretKey.key;

public class ABMGamesHandler extends AbstractHandler{
    public ABMGamesHandler(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void handle() {
// todo implementar ABM juegos
        path("/game", () -> {
            get("/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)){
                    Long gameId = Long.valueOf(req.params(":gameId"));
                    EntityManager em = emf.createEntityManager();
                    Game game = (Game) em.createQuery("FROM Game g WHERE g.id = ?1")
                            .setParameter(1, gameId)
                            .getSingleResult();
                    @SuppressWarnings("unchecked") List<UserGame> userGames = em.createQuery("FROM UserGame ug WHERE ug.gameId = ?1")
                            .setParameter(1, gameId)
                            .getResultList();
                    float meanScore = 0;
                    for (UserGame userGame : userGames) {
                        meanScore += userGame.getScore();
                    }
                    meanScore = meanScore / userGames.size();
                    List<UserForResponse> usersForResponse = new ArrayList<>();
                    for (User creator : game.getCreators()) {
                        usersForResponse.add(new UserForResponse(creator));
                    }
                    List<TagForResponse> tagsForResponse = new ArrayList<>();
                    for (Tag tag : game.getTags()) {
                        tagsForResponse.add(new TagForResponse(tag));
                    }
                    Gson gson = new Gson();
                    HardGameForResponse gameForResponse = new HardGameForResponse(gameId, meanScore, game.getFollowers().size(), game.getTitle(), game.getDescription(), game.getImgsInCarousel(), game.getWiki(), usersForResponse, tagsForResponse, game.getGameUpdates());
                    res.status(200);
                    return gson.toJson(gameForResponse);
                }else {
                    res.status(401);
                    return "{\"message\":\"Unauthorized.\"}";
                }
            });

            post("/comment/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    Long gameId = Long.valueOf(req.params(":gameId"));
                    EntityManager em = emf.createEntityManager();
                    Game game;
                    try {
                        game = (Game) em.createQuery("FROM Game g WHERE g.id = ?1")
                                .setParameter(1, gameId)
                                .getSingleResult();
                    } catch (NoResultException e) {
                        res.status(400);
                        return "{\"message\":\"Game not found.\"}";
                    }
                    Gson gson = new Gson();
                    CommentRequest commentRequest = gson.fromJson(req.body(), CommentRequest.class);
                    Comment comment = new Comment(userId, gameId, commentRequest.getContent());
                    game.addComment(comment);
                    try{
                        em.getTransaction().begin();
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
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.ABM_GAMES;
    }
}
