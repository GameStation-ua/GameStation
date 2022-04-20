package webpage.entity;

import javax.persistence.*;
import java.util.Set;


@Entity
public class User implements Actor{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column (name = "NICK_NAME", nullable = false)
    private String nickname;

    @Column (name = "USER_NAME", nullable = false)
    private String username;

    @Column (name = "PASSWORD", nullable = false)
    private String password;

    @Column (name = "IS_ADMIN", nullable = false)
    private boolean isAdmin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userId")
    private Set<UserGame> userGame;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "GAME_FOLLOWERS")
    private Set<Game> followedGames;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "THREAD_FOLLOWERS")
    private Set<Thread> followedThreads;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "USER_FOLLOWERS")
    private Set<User> followedUsers;

    @ManyToMany(mappedBy = "followedUsers")
    private Set<User> followers;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "GAME_CREATORS")
    private Set<Game> createdGames;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tag> likedTags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userId")
    private Set<Comment> commentsMade;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entityId")
    private Set<Comment> profileComments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creatorId")
    private Set<Thread> createdThreads;

    public User(String nickname, String username, String password) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public void removeTag(Tag tag){
        likedTags.remove(tag);
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

    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    @Override
    public boolean sendNotification(Notification notification, EntityManager em) {
        try {
            em.getTransaction().begin();
            for (User follower : followers) {
                follower.addNotification(notification);
            }
            em.getTransaction().commit();
            return true;
        }catch (Throwable e){
            return false;
        }
    }

    @Override
    public Set<User> getFollowers() {
        return followers;
    }

    @Override
    public String getName() {
        return nickname;
    }
}
