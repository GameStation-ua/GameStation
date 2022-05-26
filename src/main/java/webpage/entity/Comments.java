package webpage.entity;

import webpage.model.Comment;
import webpage.model.User;
import webpage.responseFormats.CommentResponse;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webpage.entity.Users.findUserById;
import static webpage.util.EntityManagers.close;
import static webpage.util.EntityManagers.currentEntityManager;

public class Comments {

    public static Optional<Integer> findCommentVotesByUserIdAndCommentId(Long userId, Long commentId){
        EntityManager em = currentEntityManager();
        try {
            Integer vote = (Integer) em.createQuery("SELECT vote FROM UserComment uc WHERE uc.userId = ?1 AND uc.commentId = ?2")
                    .setParameter(1, userId)
                    .setParameter(2, commentId)
                    .getSingleResult();
            return Optional.of(vote);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static Optional<Comment> findCommentById(Long commentId){
        EntityManager em = currentEntityManager();
        try {
            Comment comment = (Comment) em.createQuery("FROM Comment c WHERE c.id = ?1")
                    .setParameter(1, commentId)
                    .getSingleResult();
            return Optional.of(comment);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            close();
        }
    }

    public static List<CommentResponse> createCommentResponseList(List<Comment> comments, Long userId){
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (Comment comment : comments) {

            Optional<Integer> vote = findCommentVotesByUserIdAndCommentId(userId, comment.getId());

            Optional<User> user = findUserById(userId);

            if (user.isEmpty()) continue;
            if (vote.isEmpty()) {
                commentResponseList.add(new CommentResponse(comment, user.get().getNickname(), 0));
                continue;
            }

            commentResponseList.add(new CommentResponse(comment, user.get().getNickname(), vote.get()));
        }
        return commentResponseList;
    }


}
