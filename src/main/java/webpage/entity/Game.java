package webpage.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IMGS_IN_CAROUSEL")
    private int imgsInCarousel = 0;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> followers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    private List<UserGame> userGames;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> creators;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tag> tags;

    public Game() {
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

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<UserGame> getUserGames() {
        return userGames;
    }

    public void setUserGames(List<UserGame> userGames) {
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
