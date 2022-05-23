package webpage.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class GameUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "DATE", nullable = false)
    private final Date date = new Date();

    public GameUpdate(Long gameId, String title, String content) {
        this.gameId = gameId;
        this.title = title;
        this.content = content;
    }

    public GameUpdate() {
    }

    public Long getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
