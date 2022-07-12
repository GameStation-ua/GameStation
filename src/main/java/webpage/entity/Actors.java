package webpage.entity;

import webpage.model.Actor;
import webpage.model.Comment;
import webpage.model.Notification;
import webpage.model.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static webpage.entity.Persister.merge;
import static webpage.handlers.NotificationHandler.sendNotification;
import static webpage.util.EntityManagers.close;
import static webpage.util.EntityManagers.currentEntityManager;

public class Actors {


    public static Optional<Actor> findActorById(Long id){
        EntityManager em = currentEntityManager();
        try {
            return Optional.of(em.find(Actor.class, id));
        }catch (NullPointerException e){
            return Optional.empty();
        }
    }

    public static List<Comment> fetchComments(Long id){
        EntityManager em = currentEntityManager();
            @SuppressWarnings("unchecked") List<Comment> comments = em.createQuery("SELECT comments FROM ACTOR a WHERE a.id = ?1")
                    .setParameter(1, id)
                    .getResultList();
            return comments;
    }

    public static Optional<List<Comment>> findCommentsFromActorById(Long id, int pageNumber){
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Comment> comments = em.createQuery("SELECT comments FROM ACTOR a WHERE a.id = ?1")
                    .setParameter(1, id)
                    .setFirstResult(pageNumber * 10 - 10)
                    .setMaxResults(10)
                    .getResultList();
            return Optional.of(comments);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static boolean findIfFollowing(Long actorId, Long userId){
        EntityManager em = currentEntityManager();
        try {
            Integer bool = (Integer) em.createNativeQuery("SELECT 0 FROM USER_ACTOR WHERE FOLLOWERS_ID = ?1 AND FOLLOWEDACTORS_ID = ?2")
                    .setParameter(1, userId)
                    .setParameter(2, actorId)
                    .getSingleResult();
            return true;
        }catch (Exception e){
            return false;
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

    public static void sendNotificationToFollowers(Notification notification, Actor actor) {
        for (User follower : actor.getFollowers()) {
            sendNotification(follower.getId(), notification);
        }
    }

    public static boolean persistNotificationToFollowers(Notification notification, Actor actor) {
        try {
            for (User follower : actor.getFollowers()) {
                follower.addNotification(notification);
                merge(follower);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
