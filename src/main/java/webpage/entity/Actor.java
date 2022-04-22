package webpage.entity;

import javax.persistence.*;
import java.util.Set;
@Entity(name = "ACTOR")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(mappedBy = "followedActors")
    private Set<User> followers;

    private String name;


    public void setFollowers(Set<User> followers) {
        this.followers = followers;
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

    public Set<User> getFollowers(){
        return followers;
    }

    public String getName(){
        return null;
    }
}
