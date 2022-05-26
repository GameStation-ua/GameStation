package webpage.entity;

import javax.persistence.EntityManager;
import java.util.List;
import static webpage.util.EntityManagers.close;
import static webpage.util.EntityManagers.currentEntityManager;

public class Votes {

    public static Integer getVotesByCommentId(Long id) {
        EntityManager em = currentEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Integer> votes = em.createQuery("SELECT c.vote FROM UserComment c WHERE c.commentId = ?1")
                    .setParameter(1, id)
                    .getResultList();
            int sum = 0;
            for (Integer vote : votes) {
                sum += vote;
            }
            return sum;
        } catch (Exception e) {
            return Integer.MIN_VALUE;
        } finally {
            close();
        }
    }
}

