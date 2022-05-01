package webpage.entity;


import webpage.model.Thread;
import webpage.model.User;
import webpage.responseFormats.ThreadResponse;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webpage.entity.Users.findUserById;
import static webpage.util.EntityManagers.createEntityManager;

public class Threads {

    public static Optional<Thread> findThreadById(Long id){
        EntityManager em = createEntityManager();
        try {
            return Optional.of(em.find(Thread.class, id));
        }catch (NullPointerException e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }
    public static Optional<List<Thread>> findThreadsByForumPage(Long gameId, Integer pageNumber){
        EntityManager em = createEntityManager();
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
            em.close();
        }
    }

    public static List<ThreadResponse> prepareSoftThreadResponse(List<Thread> threads) {
        List<ThreadResponse> softThreadsResponse = new ArrayList<>();
        for (Thread thread : threads) {
            Optional<User> user = findUserById(thread.getCreatorId());

            softThreadsResponse.add(new ThreadResponse(thread, user.get()));
        }
        return softThreadsResponse;
    }
}
