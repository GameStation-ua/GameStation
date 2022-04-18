package webpage.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @Column(name = "TAG", nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Game> games;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Tag other = (Tag) obj;
        return name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (name == null ? 0 : name.hashCode());
        return hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String tag) {
        this.name = tag;
    }
}
