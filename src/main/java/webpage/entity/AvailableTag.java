package webpage.entity;

import javax.persistence.*;

@Entity
public class AvailableTag {

    @Id
    @Column(name = "AVAILABLE_TAG", nullable = false)
    private String availableTag;


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final AvailableTag other = (AvailableTag) obj;
        return availableTag.equals(other.getAvailableTag());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (availableTag == null ? 0 : availableTag.hashCode());
        return hash;
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
