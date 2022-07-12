package webpage.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static webpage.entity.Actors.fetchComments;
import static webpage.entity.Actors.findCommentsFromActorById;

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
        if (comments == null){
            comments = new HashSet<>(fetchComments(id));
        }
        comments.add(comment);
    }

    public Set<User> getFollowers(){
        return followers;
    }

    public String getName(){
        return name;
    }
}
