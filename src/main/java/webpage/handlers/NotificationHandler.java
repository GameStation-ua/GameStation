package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webpage.entity.Notification;
import webpage.entity.User;
import webpage.responseFormats.NotificationResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static webpage.util.EntityManagers.currentEntityManager;
import static webpage.util.SecretKey.key;


@WebSocket
public class NotificationHandler {

    private static final Map<Long, Session> sessionMap = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    public void onConnect(Session user){
        try {
            checkRegistered(user);
            sendNotifications(user);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessege(Session user, String text){
    }

    static public void sendNotification(Long userId, Notification notification){
        Session session = sessionMap.get(userId);
        try {
            session.getRemote().sendString(new Gson().toJson(new NotificationResponse(notification)));
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void sendNotifications(Session user) {
        try{
            String token = user.getUpgradeRequest().getHeader("token");
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
            User user1 = (User) currentEntityManager().createQuery("FROM User u WHERE u.id = ?1")
                    .setParameter(1, Long.valueOf((Integer) claims.get("id")))
                    .getSingleResult();
            List<NotificationResponse> notificationResponse = new ArrayList<>();
            for (Notification notification : user1.getNotifications()) {
                if (!notification.isSeen()) {
                    notificationResponse.add(new NotificationResponse(notification));
                }
            }
        Gson gson = new Gson();
        String s = gson.toJson(notificationResponse);
            user.getRemote().sendString(s);
        }catch (Throwable e){
            onError(user, e);
        }
    }

    private void checkRegistered(Session user) {
        if (!sessionMap.containsValue(user)) {
            String token = user.getUpgradeRequest().getHeader("token");
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
            sessionMap.put(Long.valueOf((Integer)claims.get("id")), user);
        }
    }


    @OnWebSocketError
    public void onError(Session user, Throwable error){
        user.getUpgradeResponse().setHeader("error", error.getMessage());
    }
}
