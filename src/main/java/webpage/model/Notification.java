package webpage.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(mappedBy = "notifications")
    private Set<User> users;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "DATE", nullable = false)
    private final Date date = new Date();

    @Column(name = "PATH", nullable = false)
    private String path;


    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public Notification(NotificationType notType, Actor actor, Actor additional, String path){
        this.path = path;
        switch (notType){
            case USER_COMMENTED_ON_PROFILE: this.content = actor.getName() + " posted a comment on your profile."; break;  // done
            case FOLLOWED_USER_COMMENTS: this.content = actor.getName() + " posted a comment on " + additional.getName() + "."; break;  // done
            case USER_STARTED_FOLLOWING: this.content = actor.getName() + " started following you."; break;  // done
            case GAME_POSTED_UPDATE: this.content = actor.getName() + " posted an update."; break; 
            case USER_COMMENTED_ON_USER_THREAD:
            case USER_COMMENTED_ON_FOLLOWED_THREAD:
                this.content = actor.getName() + " commented on " + additional.getName() + "."; break;  //done
            default:
        }
    }
}
