package webpage.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column (name = "NICK_NAME")
    private String nickname;

    @Column (name = "USER_NAME")
    private String username;

    @Column (name = "PASSWORD")
    private String password;

    @Column (name = "IS_ADMIN")
    private boolean isAdmin;

    @ManyToMany (cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "FOLLOWERS")
    private List<User> followers;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "userId")
    private List<UserGame> userGame;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "userId")
    private List<Notification> notifications;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "GAME_CREATORS")
    private List<Game> createdGames;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Tag> likedTags;

    public User(String nickname, String username, String password) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }

    public User() {

    }

    public Set<Tag> getLikedTags() {
        return likedTags;
    }

    public void setLikedTags(Set<Tag> likedtags) {
        this.likedTags = likedtags;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
