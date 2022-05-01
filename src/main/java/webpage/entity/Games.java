package webpage.entity;

import webpage.model.Game;
import webpage.model.Tag;
import webpage.model.UserGame;
import webpage.responseFormats.GameResponse;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webpage.util.EntityManagers.createEntityManager;

public class Games {


    public static Optional<Game> findGameById(Long id){
        EntityManager em = createEntityManager();
        try {
            return Optional.of(em.find(Game.class, id));
        }catch (NullPointerException e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<UserGame>> findUserGamesbyGameId(Long id){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<UserGame> userGames = em.createQuery("FROM UserGame ug WHERE ug.gameId = ?1")
                    .setParameter(1, id)
                    .getResultList();
            return Optional.of(userGames);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<Game>> findGameByTagName(Tag tag){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Game> gamesTag1 = em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1")
                    .setMaxResults(5)
                    .setParameter(1, tag.getName())
                    .getResultList();
            return Optional.of(gamesTag1);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<GameResponse>> searchStringInGames(String searchTag){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Game> games = em.createQuery("FROM Game g WHERE UPPER(g.name) LIKE ?1")
                    .setParameter(1, "%" + searchTag.toUpperCase() + "%")
                    .getResultList();
            List<GameResponse> gamesForResponse = new ArrayList<>();
            for (Game game : games) {
                gamesForResponse.add(new GameResponse(game));
            }
            return Optional.of(gamesForResponse);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }
    public static void addTagsToGame(List<Tag> tags, Game game) {
        for (Tag tag : tags) {
            game.addTag(tag);
        }
    }
    public static void removeTagsFromGame(List<Tag> tags, Game game){
        for (Tag tag : tags) {
            game.removeTag(tag);
        }
    }

    public static Optional<List<Game>> searchGameByTag(String searchTag){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Game> games = em.createQuery("SELECT games FROM Tag t WHERE t.name = :search")
                    .setParameter("search", searchTag)
                    .getResultList();
            return Optional.of(games);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }
    public static List<GameResponse> getGameResponses(List<Game> games) {
        List<GameResponse> gamesForResponse = new ArrayList<>();
        for (Game game : games) {
            gamesForResponse.add(new GameResponse(game));
        }
        return gamesForResponse;
    }
}
