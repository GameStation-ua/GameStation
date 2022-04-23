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
        path("", () -> {
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


        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.ABM_GAMES;
    }
}
