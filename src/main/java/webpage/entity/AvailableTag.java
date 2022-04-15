package webpage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AvailableTag {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "AVAILABLE_TAG")
    private String availableTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
