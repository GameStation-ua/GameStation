package webpage.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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

    @Column(name = "ACTOR_ID")
    private Long actorId;

    @Column(name = "VOTES", nullable = false)
    private int votes = 0;

    @ManyToMany(mappedBy = "likedComments")
    private Set<User> userWhoLiked = new HashSet<>();

    public Comment(Long userId, Long actorId, String content) {
        this.userId = userId;
        this.actorId = actorId;
        this.content = content;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
