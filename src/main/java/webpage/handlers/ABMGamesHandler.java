package webpage.handlers;

import com.google.gson.Gson;
import webpage.entity.Game;
import webpage.entity.Tag;
import webpage.entity.User;
import webpage.entity.UserGame;
import webpage.responseFormats.HardGameForResponse;
import webpage.responseFormats.TagResponse;
import webpage.responseFormats.UserResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.path;

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
                    if (!userGames.isEmpty()) {
                        for (UserGame userGame : userGames) {
                            meanScore += userGame.getScore();
                        }
                        meanScore = meanScore / userGames.size();
                    }else {
                        meanScore = -1;
                    }
                    List<UserResponse> usersForResponse = new ArrayList<>();
                    for (User creator : game.getCreators()) {
                        usersForResponse.add(new UserResponse(creator));
                    }
                    List<TagResponse> tagsForResponse = new ArrayList<>();
                    for (Tag tag : game.getTags()) {
                        tagsForResponse.add(new TagResponse(tag));
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
