package webpage.model;


import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "COMMENTER_ID", nullable = false)
    private Long userId;

    @Column(name = "DATE", nullable = false)
    private final Date date = new Date();

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "ACTOR_ID", nullable = false)
    private Long actorId;

    @Transient
    private int votes = Integer.MIN_VALUE;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "commentId")
    private Set<UserComment> usersWhoLiked;

    public Comment(Long userId, Long actorId, String content) {
        this.userId = userId;
        this.actorId = actorId;
        this.content = content;
    }

    public Comment() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public int getVotes() {
        if (votes == Integer.MIN_VALUE) return votes;
        else {
return 0; // todo
        }
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
