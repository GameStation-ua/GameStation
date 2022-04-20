package webpage.entity;

import javax.persistence.*;

@Entity
public class AvailableTag {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "AVAILABLE_TAG", nullable = false)
    private String availableTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AvailableTag(String availableTag) {
        this.availableTag = availableTag;
    }

    public AvailableTag() {

    }

    public String getAvailableTag() {
        return availableTag;
    }
}
