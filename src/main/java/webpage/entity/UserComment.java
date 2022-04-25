package webpage.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class UserComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "COMMENT_ID", nullable = false)
    private Long commentId;

    @Column(name = "VOTE", nullable = false)
    private Integer vote;

    public UserComment(Long userId, Long commentId, Integer vote) {
        this.userId = userId;
        this.commentId = commentId;
        this.vote = vote;
    }

    public UserComment() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
