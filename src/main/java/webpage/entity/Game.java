package webpage.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Game implements Actor{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "WIKI")
    private String wiki;

    @Column(name = "IMGS_IN_CAROUSEL", nullable = false)
    private int imgsInCarousel = 0;

    @ManyToMany(mappedBy = "followedGames")
    private Set<User> followers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    private Set<UserGame> userGames;

    @ManyToMany(mappedBy = "createdGames")
    private Set<User> creators;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entityId")
    private Set<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    private Set<GameUpdate> gameUpdates;

    public Game() {
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<User> getFollowers() {
        return followers;
    }

    @Override
    public String getName() {
        return title;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<UserGame> getUserGames() {
        return userGames;
    }

    public void setUserGames(Set<UserGame> userGames) {
        this.userGames = userGames;
    }

    public Set<User> getCreators() {
        return creators;
    }

    public void setCreators(Set<User> creators) {
        this.creators = creators;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgsInCarousel() {
        return imgsInCarousel;
    }

    public void setImgsInCarousel(int imgsInCarousel) {
        this.imgsInCarousel = imgsInCarousel;
    }
}
