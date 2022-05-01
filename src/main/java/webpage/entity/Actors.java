package webpage.entity;

import webpage.model.Actor;
import webpage.model.Comment;
import webpage.model.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static webpage.util.EntityManagers.createEntityManager;

public class Actors {


    public static Optional<Actor> findActorById(Long id){
        EntityManager em = createEntityManager();
        try {
            return Optional.of(em.find(Actor.class, id));
        }catch (NullPointerException e){
            return Optional.empty();
        }
    }

    public static Optional<List<Comment>> findCommentsFromActorById(Long id, int pageNumber){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Comment> comments = em.createQuery("SELECT comments FROM ACTOR a WHERE a.id = ?1")
                    .setParameter(1, id)
                    .setFirstResult(pageNumber * 10 - 10)
                    .setMaxResults(10)
                    .getResultList();
            return Optional.of(comments);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public static boolean checkIfFollows(Long userId, Actor actor){
        boolean alreadyFollows = false;
        for (User follower : actor.getFollowers()) {
            if (Objects.equals(follower.getId(), userId)){
                alreadyFollows = true;
                break;
            }
        }
        return alreadyFollows;
    }
}
