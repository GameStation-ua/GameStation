package webpage.entity;

import javax.persistence.*;

@Entity
public class Tag {
    @Id
    @Column(name = "TAG", nullable = false)
    private String tag;
}
