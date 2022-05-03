package webpage.model;

import javax.persistence.*;
import java.util.Set;

import static webpage.handlers.NotificationHandler.sendNotification;
import static webpage.util.EntityManagers.createEntityManager;

@Entity(name = "ACTOR")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(mappedBy = "followedActors")
    private Set<User> followers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "actorId")
    @OrderBy(value = "date ASC")
    private Set<Comment> comments;

    private String name;


    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public boolean isUser() {
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public void persistNotificationToFollowers(Notification notification) {
        EntityManager em = createEntityManager();
            em.getTransaction().begin();
            for (User follower : followers) {
                follower.addNotification(notification);
                em.merge(follower);
            }
            em.getTransaction().commit();
    }

    public Set<User> getFollowers(){
        return followers;
    }

    public String getName(){
        return name;
    }

    public void sendNotificationToFollowers(Notification notification) {
        for (User follower : followers) {
            sendNotification(follower.getId(), notification);
        }

    }
}
