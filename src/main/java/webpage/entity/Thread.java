package webpage.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Thread implements Actor{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "CREATOR_ID", nullable = false)
    private long creatorId;

    @Column(name = "FORUM_ID", nullable = false)
    private Long forumId;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DATE", nullable = false)
    private Date Date = new Date();

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entityId")
    private Set<Comment> comments;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "followedThreads")
    private Set<User> followers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean sendNotification(Notification notification, EntityManager em) {
        try {
            em.getTransaction().begin();
            for (User follower : followers) {
                follower.addNotification(notification);
            }
            em.getTransaction().commit();
            return true;
        }catch (Throwable e){
            return false;
        }
    }

    @Override
    public Set<User> getFollowers() {
        return followers;
    }

    @Override
    public String getName() {
        return title;
    }
}
