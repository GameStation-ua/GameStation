package webpage.entity;

import javax.persistence.*;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "USER_ID")
    long userId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PATH")
    private String path;

    public Long getId() {
        return id;
    }

    public Notification(String title, String description, String path) {
        this.title = title;
        this.description = description;
        this.path = path;
    }

    public Notification() {
    }
}
