package webpage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @Column(name = "TAG", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Game> games;

    @ManyToMany(mappedBy = "likedTags")
    private Set<User> users;

    @ManyToMany(mappedBy = "tags")
    private Set<GameRequest> gameRequestsWithTag;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

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

    public void addGameRequest(GameRequest gameRequest){
        gameRequestsWithTag.add(gameRequest);
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<GameRequest> getGameRequestsWithTag() {
        return gameRequestsWithTag;
    }

    public void setGameRequestsWithTag(Set<GameRequest> gameRequestsWithTag) {
        this.gameRequestsWithTag = gameRequestsWithTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String tag) {
        this.name = tag;
    }
}
