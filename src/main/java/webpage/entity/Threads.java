package webpage.entity;


import webpage.model.Thread;
import webpage.model.User;
import webpage.responseFormats.ThreadResponse;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webpage.entity.Users.findUserById;
import static webpage.util.EntityManagers.currentEntityManager;
import static webpage.util.EntityManagers.close;

public class Threads {

    public static Optional<Thread> findThreadById(Long id){
        EntityManager em = currentEntityManager();
        try {
            return Optional.of(em.find(Thread.class, id));
        }catch (NullPointerException e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<Thread> findThreadByIdJFComments(Long id){
        EntityManager em = currentEntityManager();
        try {
            Thread thread = (Thread) em.createQuery("SELECT distinct t FROM Thread t join fetch t.comments WHERE t.id = ?1")
                    .setParameter(1, id)
                    .getSingleResult();
            return Optional.of(thread);
        }catch (NullPointerException e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<Thread> findThreadByIdJFFollowers(Long id){
        EntityManager em = currentEntityManager();
        try {
            Thread thread = (Thread) em.createQuery("SELECT distinct t FROM Thread t join fetch t.followers WHERE t.id = ?1")
                    .setParameter(1, id)
                    .getSingleResult();
            return Optional.of(thread);
        }catch (NullPointerException e){
            return Optional.empty();
        }finally {
            close();
        }
    }
    public static Optional<List<Thread>> findThreadsByForumPage(Long gameId, Integer pageNumber){
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Thread> threads = em.createQuery("FROM Thread t WHERE t.gameId = ?1")
                    .setParameter(1, gameId)
                    .setFirstResult(pageNumber * 10 - 10)
                    .setMaxResults(10)
                    .getResultList();
            return Optional.of(threads);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<List<ThreadResponse>> prepareSoftThreadResponse(List<Thread> threads) {
        List<ThreadResponse> softThreadsResponse = new ArrayList<>();
        for (Thread thread : threads) {
            Optional<User> user = findUserById(thread.getCreatorId());
            if (user.isEmpty()) return Optional.empty();

            softThreadsResponse.add(new ThreadResponse(thread, user.get()));
        }
        return Optional.of(softThreadsResponse);
    }
}
