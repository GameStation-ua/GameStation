package webpage.model;

import org.hibernate.LazyInitializationException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static webpage.entity.Users.*;


@Entity
public class User extends Actor {

    @Column(name = "USER_NAME", nullable = false)
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "IS_ADMIN", nullable = false)
    private boolean isAdmin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userId")
    private Set<UserGame> userGame;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userId")
    private Set<UserComment> votedComments;

    @ManyToMany(cascade = CascadeType.MERGE)
    @OrderBy(value = "date DESC")
    private Set<Notification> notifications;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creatorId")
    private Set<GameRequest> gameRequests;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creatorId")
    private Set<Game> createdGames;

    @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Actor> followedActors;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Tag> likedTags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userId")
    private Set<Comment> commentsMade;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "creatorId")
    private Set<Thread> createdThreads;

    public User(String nickname, String username, String password) {
        super.setName(nickname);
        this.username = username;
        this.password = password;
    }

    public User(String nickname, String username, String password, String email) {
        super.setName(nickname);
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public User() {
    }

    public void addTag(Tag tag) {
        try {
            likedTags.add(tag);
        }catch (LazyInitializationException e){
            likedTags = new HashSet<>(fetchLikedTags(getId()));
            likedTags.add(tag);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserGame> getUserGame() {
        return userGame;
    }

    public void setUserGame(Set<UserGame> userGame) {
        this.userGame = userGame;
    }

    public Set<UserComment> getVotedComments() {
        return votedComments;
    }

    public void setVotedComments(Set<UserComment> votedComments) {
        this.votedComments = votedComments;
    }

    public Set<Notification> getNotifications() {
        try {
            notifications.size();
            return notifications;
        } catch (LazyInitializationException e) {
            notifications = new HashSet<>(fetchNotifications(getId()));
            return notifications;
        }
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<GameRequest> getGameRequests() {
        return gameRequests;
    }

    public void setGameRequests(Set<GameRequest> gameRequests) {
        this.gameRequests = gameRequests;
    }

    public Set<Game> getCreatedGames() {
        return createdGames;
    }

    public void setCreatedGames(Set<Game> createdGames) {
        this.createdGames = createdGames;
    }

    public Set<Actor> getFollowedActors() {
        return followedActors;
    }

    public void setFollowedActors(Set<Actor> followedActors) {
        this.followedActors = followedActors;
    }

    public Set<Comment> getCommentsMade() {
        return commentsMade;
    }

    public void setCommentsMade(Set<Comment> commentsMade) {
        this.commentsMade = commentsMade;
    }

    public Set<Thread> getCreatedThreads() {
        return createdThreads;
    }

    public void setCreatedThreads(Set<Thread> createdThreads) {
        this.createdThreads = createdThreads;
    }

    public void removeTag(Tag tag) {
        try {
            likedTags.remove(tag);
        } catch (LazyInitializationException e) {
            likedTags = new HashSet<>(fetchLikedTags(getId()));
            likedTags.remove(tag);
        }
    }

    public Set<Tag> getLikedTags() {
        try {
            likedTags.size();
            return likedTags;
        } catch (LazyInitializationException e) {
            likedTags = new HashSet<>(fetchLikedTags(getId()));
            return likedTags;
        }
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

    public Long getId() {
        return super.getId();
    }

    public String getNickname() {
        return super.getName();
    }

    public void setNickname(String nickname) {
        super.setName(nickname);
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
        try {
            notifications.add(notification);
        }catch (LazyInitializationException e){
            notifications = new HashSet<>(fetchNotifications(getId()));
            notifications.add(notification);
        }
    }

    public void addFollowedActor(Actor actor) {
        try {
            followedActors.add(actor);
        }catch(LazyInitializationException e){
            followedActors = new HashSet<>(fetchFollowedActors(getId()));
            followedActors.add(actor);
        }
    }

    public void removeFollowedActor(Actor actor) {
        try {
            followedActors.remove(actor);
        }catch(LazyInitializationException e){
            followedActors = new HashSet<>(fetchFollowedActors(getId()));
            followedActors.remove(actor);
        }
    }
}
