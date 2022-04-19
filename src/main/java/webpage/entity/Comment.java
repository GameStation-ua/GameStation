package webpage.entity;



import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "COMMENTER_ID")
    private Long userId;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "CONTENT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "TARGET_ENTITY_TYPE", nullable = false)
    private EntityType entityType;

    @Column(name = "ENTITY_ID", nullable = false)
    private Long entityId;

    @Column(name = "VOTES")
    private int votes = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
