package webpage.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(mappedBy = "notifications")
    private Set<User> users;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "PATH", nullable = false)
    private String path;

    @Column(name = "SEEN", nullable = false)
    private boolean seen = false;


    public Notification() {
    }
}
