package webpage.entity;

import webpage.model.AvailableTag;
import webpage.model.Tag;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webpage.entity.Persister.merge;
import static webpage.entity.Persister.remove;
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

    public static Optional<List<AvailableTag>> findAvailableTags(){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<AvailableTag> availableTags = em.createQuery("FROM AvailableTag")
                    .getResultList();
            return Optional.of(availableTags);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static boolean tagsExist(List<Tag> tags){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<String> availableTags = em.createQuery("SELECT availableTag FROM AvailableTag")
                    .getResultList();
            for (Tag tag : tags) {
                if (!availableTags.contains(tag.getName())) return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }finally {
            em.close();
        }
    }
    public static void removeAvailableTags(List<AvailableTag> tags, List<AvailableTag> availableTags) {
        for (AvailableTag tagsRequestTag : tags) {
            if (availableTags.contains(tagsRequestTag)) {
                remove(tagsRequestTag);
            }
        }
    }

    public static void addAvailableTags(List<AvailableTag> tags, List<AvailableTag> availableTags) {
        for (AvailableTag tagsRequestTag : tags) {
            if (!availableTags.contains(tagsRequestTag)) {
                merge(Optional.of(tagsRequestTag));
            }
        }
    }
    public static Optional<List<Tag>> findTagsIfAvailable(List<Tag> tags){
        if (!tagsExist(tags)) return Optional.empty();
        List<Tag> tags1 = new ArrayList<>();
        for (Tag tag : tags) {
            Optional<Tag> tag1 = findTagByName(tag.getName());
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
