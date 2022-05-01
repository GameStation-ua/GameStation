package webpage.entity;

import webpage.model.UserGame;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static webpage.util.EntityManagers.createEntityManager;

public class UserGames {

    public static Optional<List<UserGame>> findUserGameByUserId(Long userId){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<UserGame> gameList = em.createQuery("FROM UserGame ug WHERE ug.userId = ?1")
                    .setParameter(1, userId)
                    .getResultList();
            return Optional.of(gameList);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<Long>> findGameIdsByUserId(Long userId){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Long> gamesIds = em.createQuery("SELECT gameId FROM UserGame ug WHERE ug.userId = ?1")
                    .setParameter(1, userId)
                    .getResultList();
            return Optional.of(gamesIds);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<UserGame> findUserGameByUserIdAndGameId(Long gameId, Long userId){
        EntityManager em = createEntityManager();
        try {
            UserGame game = (UserGame) em.createQuery("FROM UserGame ug WHERE ug.gameId = ?1 AND ug.userId = ?2")
                    .setParameter(1, gameId)
                    .setParameter(2, userId)
                    .getSingleResult();
            return Optional.of(game);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }
}
