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

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "WIKI", nullable = false)
    private String wiki;

    @Column(name = "IMGS_IN_CAROUSEL", nullable = false)
    private Integer imgsInCarousel;

    @Column(name = "DATE", nullable = false)
    private Date date = new Date();

    @ManyToMany(mappedBy = "gameRequests")
    private Set<User> gameCreators;

    @ManyToMany(mappedBy = "gameRequestsWithTag")
    private Set<Tag> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
