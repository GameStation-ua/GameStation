package webpage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Thread extends Actor{

    @Column(name = "CREATOR_ID", nullable = false)
    private long creatorId;

    @Column(name = "GAME_ID", nullable = false)
    private Long gameId;

    @Column(name = "DATE", nullable = false)
    private Date date = new Date();

    @Column(name = "DESCRIPTION", nullable = false, length = 8000)
    private String description;

    public Thread(long creatorId, Long gameId, String description, String name) {
        this.creatorId = creatorId;
        this.gameId = gameId;
        this.description = description;
        setName(name);
    }

    public Thread() {
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return super.getName();
    }

    public void setTitle(String title) {
        super.setName(title);
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }
}
