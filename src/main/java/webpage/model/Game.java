package webpage.model;

import org.hibernate.LazyInitializationException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static webpage.entity.Games.fetchTags;

import static webpage.entity.Users.fetchLikedTags;

@Entity
public class Game extends Actor{

    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @Column(name = "DESCRIPTION", nullable = false, length = 8000)
    private String description;

    @Column(name = "WIKI")
    private String wiki;

    @Column(name = "IMGS_IN_CAROUSEL", nullable = false)
    private int imgsInCarousel = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    @OrderBy(value = "date ASC")
    private Set<Thread> threads;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    private Set<UserGame> userGames;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    @OrderBy(value = "date ASC")
    private Set<GameUpdate> gameUpdates;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "gameId")
    private Set<GameRequest> editRequests;

    public Game() {
    }

    public Game(Long creatorId,String title, String description, String wiki, int imgsInCarousel, Set<Tag> tags) {
        setName(title);
        this.creatorId = creatorId;
        this.description = description;
        this.wiki = wiki;
        this.imgsInCarousel = imgsInCarousel;
        this.tags = tags;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }


    @Override
    public Set<User> getFollowers() {
        return super.getFollowers();
    }

    public void addComment(Comment comment){
        super.addComment(comment);
    }

    public Set<GameUpdate> getGameUpdates() {
        return gameUpdates;
    }

    public void setGameUpdates(Set<GameUpdate> gameUpdates) {
        this.gameUpdates = gameUpdates;
    }

    public String getTitle() {
        return super.getName();
    }

    public void setTitle(String title) {
        super.setName(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserGame> getUserGames() {
        return userGames;
    }

    public void setUserGames(Set<UserGame> userGames) {
        this.userGames = userGames;
    }

    public Set<Tag> getTags() {
        try {
            tags.size();
            return tags;
        } catch (LazyInitializationException e) {
            tags = new HashSet<>(fetchTags(getId()));
            return tags;
        }
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void removeTag(Tag tag) {
        try {
            tags.remove(tag);
        } catch (LazyInitializationException e) {
            tags = new HashSet<>(fetchTags(getId()));
            tags.remove(tag);
        }
    }

    public void addTag(Tag tag) {
        try {
            tags.add(tag);
        } catch (LazyInitializationException e) {
            tags = new HashSet<>(fetchTags(getId()));
            tags.add(tag);
        }
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public int getImgsInCarousel() {
        return imgsInCarousel;
    }

    public void setImgsInCarousel(int imgsInCarousel) {
        this.imgsInCarousel = imgsInCarousel;
    }
}
