package webpage.entity;

import webpage.model.Game;
import webpage.model.GameRequest;
import webpage.model.Tag;
import webpage.model.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webpage.entity.Games.findGameRequestsByTag;
import static webpage.entity.Games.findGamesByTag;
import static webpage.entity.Persister.merge;
import static webpage.entity.Users.findUsersByTag;
import static webpage.util.EntityManagers.createEntityManager;

public class Tags {

    public static Optional<List<Tag>> findLikedTagsByUserId(Long userId){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Tag> tags = em.createQuery("SELECT likedTags FROM User u WHERE u.id = ?1")
                    .setParameter(1, userId)
                    .getResultList();
            return Optional.of(tags);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<Tag>> findAvailableTags(){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Tag> availableTags = em.createQuery("FROM Tag")
                    .getResultList();
            return Optional.of(availableTags);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static boolean tagsExist(List<String> tags){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<String> availableTags = em.createQuery("SELECT name FROM Tag")
                    .getResultList();
            for (String tag : tags) {
                if (!availableTags.contains(tag)) return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }finally {
            em.close();
        }
    }
    public static void removeTags(List<String> tagsToAdd) {
        Optional<List<Tag>> availableTags = findAvailableTags();
        if (availableTags.isEmpty()) {
            throw new RuntimeException("Error while fetching tags");
        }
        for (String tagToRemove : tagsToAdd) {
            boolean isContained = false;
            for (Tag tag : availableTags.get()){
                if (tag.getName().equals(tagToRemove)){
                    isContained = true;
                    break;
                }
            }
            if (isContained){
                Optional<Tag> tag = findTagByName(tagToRemove);
                if (tag.isEmpty()) throw new RuntimeException("Error while fetching tag");
                Optional<List<Game>> gamesWithTag = findGamesByTag(tag.get().getName());
                Optional<List<User>> usersWithTag = findUsersByTag(tag.get().getName());
                Optional<List<GameRequest>> gameRequestsWithTag = findGameRequestsByTag(tag.get().getName());
                if (gamesWithTag.isEmpty() || usersWithTag.isEmpty() || gameRequestsWithTag.isEmpty()) throw new RuntimeException("Error while fetching tag");
                EntityManager em = createEntityManager();
                try {
                    em.getTransaction().begin();
                    for (Game game : gamesWithTag.get()) {
                        game.removeTag(tag.get());
                        em.merge(game);
                    }
                    for (User user : usersWithTag.get()) {
                        user.removeTag(tag.get());
                        em.merge(user);
                    }
                    for (GameRequest gameRequest : gameRequestsWithTag.get()) {
                        gameRequest.removeTag(tag.get());
                        em.merge(gameRequest);
                    }
                    em.remove(em.contains(tag.get()) ? tag.get() : em.merge(tag.get()));
                    em.getTransaction().commit();
                }catch (Exception e){
                    em.getTransaction().rollback();
                    throw new RuntimeException("error while removing tags");
                }finally {
                    em.close();
                }
            }
        }
    }

    public static List<String> createTagResponseList(List<Tag> tagList){
        List<String> tagsForResponse = new ArrayList<>();
        for (Tag tag : tagList) {
            tagsForResponse.add(tag.getName());
        }
        return tagsForResponse;
    }

    public static void addTags(List<String> tagsToAdd) {
        Optional<List<Tag>> availableTags = findAvailableTags();
        if (availableTags.isEmpty()) {
            throw new RuntimeException("Error while fetching tags");
        }
        for (String tagToAdd : tagsToAdd) {
            boolean isContained = false;
            for (Tag tag : availableTags.get()){
                if (tag.getName().equals(tagToAdd)){
                    isContained = true;
                    break;
                }
            }
            if (!isContained){
                merge(new Tag(tagToAdd));
            }
        }
    }

    public static Optional<List<Tag>> findTagsIfAvailable(List<String> tags){
        if (!tagsExist(tags)) return Optional.empty();
        List<Tag> tags1 = new ArrayList<>();
        for (String tag : tags) {
            Optional<Tag> tag1 = findTagByName(tag);
            if (tag1.isEmpty()) return Optional.empty();
            tags1.add(tag1.get());
        }
        return Optional.of(tags1);
    }

    public static Optional<Tag> findTagByName(String name){
        EntityManager em = createEntityManager();
        try {
            Tag tag = (Tag) em.createQuery("FROM Tag t WHERE t.name = ?1")
                    .setParameter(1, name)
                    .getSingleResult();
            return Optional.of(tag);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }
}
