package webpage.entity;

import webpage.model.*;
import webpage.requestFormats.CreateGameRequest;
import webpage.requestFormats.EditGameRequest;
import webpage.requestFormats.GameUpdateRequest;
import webpage.responseFormats.*;

import javax.persistence.EntityManager;
import java.util.*;

import static webpage.entity.Persister.merge;
import static webpage.entity.Persister.remove;
import static webpage.entity.Tags.findTagsIfAvailable;
import static webpage.entity.Tags.tagsExist;
import static webpage.entity.Users.findCreatedGamesbyUserId;
import static webpage.util.EntityManagers.close;
import static webpage.util.EntityManagers.currentEntityManager;

public class Games {


    public static Optional<Game> findGameById(Long id){
        EntityManager em = currentEntityManager();
        try {
            return Optional.of(em.find(Game.class, id));
        }catch (NullPointerException e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<List<GameUpdate>>findGameUpdatesByPage(Long gameId, int pageNum){
        EntityManager em = currentEntityManager();
        try{
            @SuppressWarnings("unchecked") List<GameUpdate> gameUpdates = em.createQuery("FROM GameUpdate g WHERE gameId = ?1 ORDER BY g.date DESC")
                    .setParameter(1, gameId)
                    .setFirstResult(pageNum * 10 - 10)
                    .setMaxResults(10)
                    .getResultList();
            return Optional.of(gameUpdates);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static List<GameUpdateResponse> prepareGameUpdatesResponse(List<GameUpdate> gameUpdates){
        List<GameUpdateResponse> gameUpdateResponseList = new ArrayList<>();
        for (GameUpdate gameUpdate : gameUpdates) {
            gameUpdateResponseList.add(new GameUpdateResponse(gameUpdate));
        }
        return gameUpdateResponseList;
    }

    public static List<SoftGameResponse> createSoftGameResponseList(List<Game> games, Long userId){
        List<SoftGameResponse> softGameResponseList = new ArrayList<>();
        for (Game game : games) {
            softGameResponseList.add(new SoftGameResponse(game, userId));
        }
        return softGameResponseList;
    }

    public static Optional<List<String>> findTitlesByUserGames(List<UserGame> userGames){
            List<String> titles = new ArrayList<>();
            for (UserGame userGame : userGames) {
                Optional<String> title = findTitleById(userGame.getGameId());
                if (title.isEmpty()) return Optional.empty();
                else titles.add(title.get());
            }
            return Optional.of(titles);
    }

    private static Optional<String> findTitleById(Long gameId) {
        EntityManager em = currentEntityManager();
        try {
            String title = (String) em.createQuery("SELECT g.name FROM Game g WHERE g.id = ?1").setParameter(1, gameId).getSingleResult();
            return Optional.of(title);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }


    public static Optional<Game> findGameByIdJFFollowers(Long id){
        EntityManager em = currentEntityManager();
        try {
            return Optional.of((Game) em.createQuery("SELECT distinct g FROM Game g join fetch g.followers WHERE g.id = ?1")
                    .setParameter(1, id)
                    .getSingleResult());
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<List<UserGame>> findUserGamesbyGameId(Long id){
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<UserGame> userGames = em.createQuery("FROM UserGame ug WHERE ug.gameId = ?1")
                    .setParameter(1, id)
                    .getResultList();
            return Optional.of(userGames);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<List<Game>> find5GamesByTagName(Tag tag){
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Game> gamesTag1 = em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1")
                    .setMaxResults(5)
                    .setParameter(1, tag.getName())
                    .getResultList();
            return Optional.of(gamesTag1);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<List<SoftGameResponse>> searchStringInGames(String searchTag, Long userId){
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Game> games = em.createQuery("FROM Game g WHERE UPPER(g.name) LIKE ?1")
                    .setParameter(1, "%" + searchTag.toUpperCase() + "%")
                    .setMaxResults(10)
                    .getResultList();
            List<SoftGameResponse> gamesForResponse = new ArrayList<>();
            for (Game game : games) {
                gamesForResponse.add(new SoftGameResponse(game, userId));
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

    public static Optional<GameUpdate> findGameUpdateById(Long id){
        EntityManager em = currentEntityManager();
        try {
            return Optional.of(em.find(GameUpdate.class, id));
        }catch (NullPointerException e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<Game>> search50GamesByTag(String searchTag){
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Game> games = em.createQuery("SELECT games FROM Tag t WHERE t.name = :search")
                    .setParameter("search", searchTag)
                    .setMaxResults(50)
                    .getResultList();
            return Optional.of(games);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<Game>> findGamesByTag(String tag){
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Game> games = em.createQuery("SELECT games FROM Tag t WHERE t.name = ?1")
                    .setParameter(1, tag)
                    .getResultList();
            return Optional.of(games);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<GameRequest>> findGameRequestsByTag(String tag){
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<GameRequest> gameRequests = em.createQuery("SELECT gameRequestsWithTag FROM Tag t WHERE t.name = ?1")
                    .setParameter(1, tag)
                    .getResultList();
            return Optional.of(gameRequests);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static List<GameResponse> getGameResponses(List<Game> games, Long userId) {
        List<GameResponse> gamesForResponse = new ArrayList<>();
        for (Game game : games) {
            gamesForResponse.add(new GameResponse(game, userId));
        }
        return gamesForResponse;
    }

    public static Optional<GameRequest> createGameRequest(CreateGameRequest createGameRequest, Long creatorId){
        if (!tagsExist(createGameRequest.getTags())) return Optional.empty();
        if (createGameRequest.getDescription().equals("") || createGameRequest.getDescription() == null
                || createGameRequest.getTitle().equals("") || createGameRequest.getTitle() ==  null) return Optional.empty();
        Optional<List<Tag>> tags = findTagsIfAvailable(createGameRequest.getTags());
        if (tags.isEmpty()) return Optional.empty();
        Set<Tag> tags1 = new HashSet<>(tags.get());
        return Optional.of(new GameRequest(createGameRequest.getTitle(), createGameRequest.getDescription(), createGameRequest.getWiki(), creatorId, tags1, true));
    }
    public static Optional<GameRequest> editGameRequest(EditGameRequest editGameRequest, Long creatorId) {

        EntityManager em = currentEntityManager();
        Game game;
        try{
        game = em.find(Game.class, editGameRequest.getGameId());
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
        if (editGameRequest.getDescription() == null) editGameRequest.setDescription(game.getDescription());
        if (editGameRequest.getTitle() ==  null) editGameRequest.setTitle(game.getTitle());
        if (editGameRequest.getWiki() ==  null) editGameRequest.setWiki(game.getWiki());
        if (editGameRequest.getTags() ==  null) {
            List<String> tags = new ArrayList<>();
            for (Tag tag : game.getTags()) {
                tags.add(tag.getName());
            }
            editGameRequest.setTags(tags);
        }
        else if (!tagsExist(editGameRequest.getTags())) return Optional.empty();
        Optional<List<Tag>> tags = findTagsIfAvailable(editGameRequest.getTags());
        if (tags.isEmpty()) return Optional.empty();
        Set<Tag> tags1 = new HashSet<>(tags.get());
        return Optional.of(new GameRequest(editGameRequest.getTitle(), editGameRequest.getDescription(), editGameRequest.getWiki(), creatorId, tags1, false, game.getId()));
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
        EntityManager em = currentEntityManager();
        try {
             Tag tags = (Tag) em.createQuery("FROM Tag t WHERE t.name = ?1")
                    .setParameter(1, name)
                    .getResultList();
            return Optional.of(tags);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<GameRequest> findGameRequestById(Long id){
        EntityManager em = currentEntityManager();
        try {
            GameRequest gameRequest = em.find(GameRequest.class, id);
            return Optional.of(gameRequest);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Game createGameFromRequest(GameRequest gameRequest){
        Game game = new Game(gameRequest.getCreatorId(), gameRequest.getTitle(), gameRequest.getDescription(), gameRequest.getWiki(), gameRequest.getImgsInCarousel(), gameRequest.getTags());
            Game game1 = merge(game);
            remove(gameRequest);
            return game1;
    }

    public static long editGame(GameRequest gameRequest){
        Optional<Game> game = findGameById(gameRequest.getGameId());
        if (game.isEmpty()) return -1;

        setGameData(gameRequest, game.get());
        merge(game.get());
        return game.get().getId();
    }

    private static void setGameData(GameRequest gameRequest, Game game){
        game.setDescription(gameRequest.getDescription());
        game.setTitle(gameRequest.getTitle());
        game.setWiki(gameRequest.getWiki());
        game.setTags(gameRequest.getTags());
    }

    public static void createGameUpdate(GameUpdateRequest gur){
        GameUpdate gameUpdate = new GameUpdate(gur.getGameId(), gur.getTitle(), gur.getContent());
        merge(gameUpdate);
    }

    public static Optional<List<GameRequestResponse>> getGameRequestsAsResponses(){
        EntityManager em = currentEntityManager();
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
            close();
        }
    }

    public static List<GameListItem> prepareGameListResponse(List<UserGame> userGames, List<String> titles){
        List<GameListItem> gameListItems = new ArrayList<>();
        for (int i = 0; i < userGames.size(); i++) {
            gameListItems.add(new GameListItem(userGames.get(i), titles.get(i)));
        }
        return gameListItems;
    }
}
