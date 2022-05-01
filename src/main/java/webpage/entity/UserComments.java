package webpage.entity;

import webpage.model.UserComment;

import javax.persistence.EntityManager;
import java.util.Optional;

import static webpage.util.EntityManagers.createEntityManager;

public class UserComments {

    public static Optional<UserComment> findUserCommentByCommentIdAndUserId(Long commentId, Long userId){
        EntityManager em = createEntityManager();
        try {
            UserComment userComment = (UserComment) em.createQuery("FROM UserComment WHERE commentId = ?1 AND userId = ?2")
                    .setParameter(1, commentId)
                    .setParameter(2, userId)
                    .getSingleResult();
            return Optional.of(userComment);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }
}
