package webpage.entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.model.*;
import webpage.requestFormats.FollowRequest;
import webpage.responseFormats.UserResponse;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webpage.entity.Persister.merge;
import static webpage.handlers.NotificationHandler.sendNotification;
import static webpage.util.EntityManagers.createEntityManager;
import static webpage.util.SecretKey.key;

public class Users {

    public static Optional<User> findUserById(Long id){
        EntityManager em = createEntityManager();
        try {
            return Optional.of(em.find(User.class, id));
        }catch (NullPointerException e){
            return Optional.empty();
        }
    }

    public static Optional<List<Game>> findCreatedGamesbyUserId(Long id){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<Game> games = em.createQuery("SELECT createdGames FROM User u WHERE u.id = ?1")
                    .setParameter(1, id)
                    .getResultList();
            return Optional.of(games);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }


    public static void notifyIfUser(FollowRequest followRequest, Long userId, Actor actor, User user) {
        try {
            User user1 = (User) actor;
            Notification notification = new Notification(NotificationType.USER_STARTED_FOLLOWING, user, actor, followRequest.getPath());
            user1.addNotification(notification);
            merge(Optional.of(user1));
            sendNotification(userId, notification);
        } catch (Throwable ignored) {
        }
    }

    public static Optional<User> findUserByUsername(String username){
        EntityManager em = createEntityManager();
        try {
            User user = (User) em.createQuery("FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static Optional<List<UserResponse>> searchStringInUsers(String searchString){
        EntityManager em = createEntityManager();
        try {
            @SuppressWarnings("unchecked") List<User> users = em.createQuery("FROM User u WHERE UPPER(u.name) LIKE ?1")
                    .setParameter(1, "%" + searchString.toUpperCase() + "%")
                    .getResultList();
            List<UserResponse> usersForResponse = new ArrayList<>();
            for (User user : users) {
                usersForResponse.add(new UserResponse(user));
            }
            return Optional.of(usersForResponse);
        }catch (Exception e){
            return Optional.empty();
        }finally {
            em.close();
        }
    }

    public static void addTagsToUser(List<Tag> tags, User user) {
        user.getLikedTags().addAll(tags);
    }
    public static void removeTagsFromUser(List<Tag> tags, User user) {
        tags.forEach(user.getLikedTags()::remove);
    }


    public static Long getIdByToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token).getBody();
        return Long.valueOf((Integer) claims.get("id"));
    }

    public static boolean getIsAdminByToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token).getBody();
        return (boolean) claims.get("isAdmin");
    }
}
