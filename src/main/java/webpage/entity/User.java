package webpage.entity;

import javax.persistence.*;
import java.util.Set;


@Entity
public class User extends Actor{

    @Column (name = "USER_NAME", nullable = false)
    private String username;

    @Column (name = "PASSWORD", nullable = false)
    private String password;

    @Column (name = "IS_ADMIN", nullable = false)
    private boolean isAdmin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userId")
    private Set<UserGame> userGame;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> likedComments;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GameRequest> gameRequests;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "GAME_CREATORS")
    private Set<Game> createdGames;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Actor> followedActors;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tag> likedTags;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userId")
    private Set<Comment> commentsMade;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entityId")
    private Set<Comment> profileComments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creatorId")
    private Set<Thread> createdThreads;

    public User(String nickname, String username, String password) {
        super.setName(nickname);
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public Set<UserGame> getUserGame() {
        return userGame;
    }

    public void setUserGame(Set<UserGame> userGame) {
        this.userGame = userGame;
    }

    public Set<Comment> getLikedComments() {
        return likedComments;
    }

    public void setLikedComments(Set<Comment> likedComments) {
        this.likedComments = likedComments;
    }

    public Set<Notification> getNotifications() {
        return notifications;
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

    public Set<Comment> getProfileComments() {
        return profileComments;
    }

    public void setProfileComments(Set<Comment> profileComments) {
        this.profileComments = profileComments;
    }

    public Set<Thread> getCreatedThreads() {
        return createdThreads;
    }

    public void setCreatedThreads(Set<Thread> createdThreads) {
        this.createdThreads = createdThreads;
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
        notifications.add(notification);
    }


    public void addFollowedActor(Actor actor) {
        followedActors.add(actor);
    }

    public void removeFollowedActor(Actor actor) {
        followedActors.remove(actor);
    }
}
