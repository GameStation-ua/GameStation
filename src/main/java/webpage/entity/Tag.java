package webpage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tag {
    @Id
    @Column(name = "TAG", nullable = false)
    private String tag;
}
