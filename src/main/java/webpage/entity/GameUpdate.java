package webpage.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class GameUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "TITLE")
    private String tile;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "HAS_ATTACHED_IMG")
    private boolean hasAttachedImg;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
