import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Notification {
    @Column(name = "TITLE") String title;
    @Column(name = "DESCRIPTION") String description;
    @Column(name = "PATH") String path;

    public Notification(String title, String description, String path) {
        this.title = title;
        this.description = description;
        this.path = path;
    }

    public Notification() {
    }
}
