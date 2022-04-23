package webpage.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Thread extends Actor{

    @Column(name = "CREATOR_ID", nullable = false)
    private long creatorId;

    @Column(name = "GAME_ID", nullable = false)
    private Long gameId;

    @Column(name = "DATE", nullable = false)
    private Date Date = new Date();

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

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
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
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
