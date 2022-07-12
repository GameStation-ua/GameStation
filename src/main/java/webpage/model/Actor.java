package webpage.model;

import org.hibernate.LazyInitializationException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static webpage.entity.Actors.*;
import static webpage.entity.Users.fetchNotifications;

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
        try{
            comments.add(comment);
        }catch (LazyInitializationException e){
            comments = new HashSet<>(fetchComments(id));
            comments.add(comment);
        }
    }

    public Set<User> getFollowers(){
        return new HashSet<>(fetchFollowers(getId()));
    }

    public String getName(){
        return name;
    }
}
