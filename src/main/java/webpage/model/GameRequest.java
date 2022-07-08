package webpage.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class GameRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", nullable = false, length = 8000)
    private String description;

    @Column(name = "WIKI", nullable = false)
    private String wiki;

    @Column(name = "IMGS_IN_CAROUSEL", nullable = false)
    private Integer imgsInCarousel = 0;

    @Column(name = "DATE", nullable = false)
    private final Date date = new Date();

    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @Column(name = "ALREADY_EXISTS")
    private boolean alreadyExists;

    @Column(name = "GAMEID")
    private Long gameId;

    public GameRequest(String title, String description, String wiki, Long creatorId, Set<Tag> tags, boolean alreadyExists) {
        this.title = title;
        this.description = description;
        this.wiki = wiki;
        this.creatorId = creatorId;
        this.tags = tags;
        this.alreadyExists = alreadyExists;
    }
    public GameRequest(String title, String description, String wiki, Long creatorId, Set<Tag> tags, boolean alreadyExists, Long gameId) {
        this.title = title;
        this.description = description;
        this.wiki = wiki;
        this.creatorId = creatorId;
        this.tags = tags;
        this.alreadyExists = alreadyExists;
        this.gameId = gameId;
    }

    public GameRequest() {
    }

    public boolean isAlreadyExists() {
        return alreadyExists;
    }

    public void setAlreadyExists(boolean alreadyExists) {
        this.alreadyExists = alreadyExists;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void addTag(Tag tag){
        tags.add(tag);
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

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public Integer getImgsInCarousel() {
        return imgsInCarousel;
    }

    public void setImgsInCarousel(Integer imgsInCarousel) {
        this.imgsInCarousel = imgsInCarousel;
    }

    public Date getDate() {
        return date;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
