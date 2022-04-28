package webpage.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Game extends Actor{

    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "WIKI")
    private String wiki;

    @Column(name = "IMGS_IN_CAROUSEL", nullable = false)
    private int imgsInCarousel = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    @OrderBy(value = "date DESC")
    private Set<Thread> threads;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    private Set<UserGame> userGames;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gameId")
    private Set<GameUpdate> gameUpdates;

    public Game() {
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

    public void removeTag(Tag tag){
        tags.remove(tag);
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
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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

    public void addTag(Tag tag) {
        tags.add(tag);
    }
}
