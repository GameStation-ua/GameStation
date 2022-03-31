package webpage.entity;

import javax.persistence.*;

@Entity(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @Column(name = "TITLE") String title;
    @Column(name = "DESCRIPTION") String description;
    @Column(name = "PATH") String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notification(String title, String description, String path) {
        this.title = title;
        this.description = description;
        this.path = path;
    }

    public Notification() {
    }
}
