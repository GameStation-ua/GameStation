package webpage.entity;

import webpage.model.*;
import webpage.requestFormats.CreateGameRequest;
import webpage.requestFormats.GameUpdateRequest;
import webpage.responseFormats.GameRequestResponse;
import webpage.responseFormats.GameResponse;

import javax.persistence.EntityManager;
import java.util.*;

import static webpage.entity.Persister.*;
import static webpage.entity.Tags.tagsExist;
import static webpage.entity.Uploads.attachMainImgToGame;
import static webpage.entity.Uploads.upload;
import static webpage.entity.Users.findCreatedGamesbyUserId;
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

    public static Optional<Game> findGameByIdJFFollowers(Long id){
        EntityManager em = createEntityManager();
        try {
            return Optional.of((Game) em.createQuery("SELECT distinct g FROM Game g join fetch g.followers WHERE g.id = ?1 ")
                    .setParameter(1, id)
                    .getSingleResult());
        }catch (NullPointerException e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<Game> findGameByIdJFTags(Long id){
        EntityManager em = createEntityManager();
        try {
            return Optional.of((Game) em.createQuery("SELECT distinct g FROM Game g join fetch g.tags WHERE g.id = ?1 ")
                    .setParameter(1, id)
                    .getSingleResult());
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

    public static Optional<GameRequest> createGameRequest(CreateGameRequest createGameRequest, Long creatorId){
        if (!tagsExist(createGameRequest.getTags())) return Optional.empty();
        Set<Tag> tags = new HashSet<>(createGameRequest.getTags());
        return Optional.of(new GameRequest(createGameRequest.getTitle(), createGameRequest.getDescription(), createGameRequest.getWiki(), creatorId, tags));
    }

    public static boolean isOwner(Long userId, Long gameId){
        Optional<List<Game>> games = findCreatedGamesbyUserId(userId);
        if (games.isEmpty()) return false;
        for (Game game : games.get()) {
            if (game.getId().equals(gameId)) return true;
        }
        return false;
    }

    public static Optional<Tag> findTagByName(String name){
        EntityManager em = createEntityManager();
        try {
             Tag tags = (Tag) em.createQuery("FROM Tag t WHERE t.name = ?1")
                    .setParameter(1, name)
                    .getResultList();
            return Optional.of(tags);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<GameRequest> findGameRequestById(Long id){
        EntityManager em = createEntityManager();
        try {
            GameRequest gameRequest = em.find(GameRequest.class, id);
            return Optional.of(gameRequest);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<GameRequest> findGameRequestByIdJFTags(Long id){
        EntityManager em = createEntityManager();
        try {
            GameRequest gameRequest = (GameRequest) em.createQuery("SELECT distinct g FROM GameRequest g join fetch g.tags WHERE g.id = ?1 ")
                    .setParameter(1, id)
                    .getSingleResult();
            return Optional.of(gameRequest);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static void createGameFromRequest(GameRequest gameRequest){
        Game game = new Game(gameRequest.getCreatorId(), gameRequest.getTitle(), gameRequest.getDescription(), gameRequest.getWiki(), gameRequest.getImgsInCarousel(), gameRequest.getTags());
            Game game1 = merge(game);  // todo attach imgs to game
            remove(gameRequest);
    }

    public static void createGameUpdate(GameUpdateRequest gur){
        GameUpdate gameUpdate = new GameUpdate(gur.getGameId(), gur.getTitle(), gur.getContent());
        merge(gameUpdate);
    }

    public static Optional<List<GameRequestResponse>> getGameRequestsAsResponses(){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<GameRequest> gameRequests = em.createQuery("SELECT distinct g FROM GameRequest g join fetch g.tags")
                    .getResultList();
            List<GameRequestResponse> gameRequestResponses = new ArrayList<>();
            for (GameRequest gameRequest : gameRequests) {
                gameRequestResponses.add(new GameRequestResponse(gameRequest));
            }
            return Optional.of(gameRequestResponses);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }
}
