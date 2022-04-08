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
    String title;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "PATH")
    String path;

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
