package webpage.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "forumId")
    private Set<Thread> threads;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
