package webpage.handlers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webpage.model.Notification;
import webpage.model.User;
import webpage.responseFormats.NotificationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static webpage.entity.Notifications.prepareNotificationResponse;
import static webpage.entity.Users.findUserById;
import static webpage.entity.Users.getIdByToken;
import static webpage.util.Parser.toJson;
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
            session.getRemote().sendString(toJson(new NotificationResponse(notification)));
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void sendNotifications(Session user) {
        try{
        String token = user.getUpgradeRequest().getHeader("token");
        Long userId = getIdByToken(token);
        Optional<User> userOptional = findUserById(userId);

        if (userOptional.isEmpty()){
            onError(user, new Exception("User not found"));
        }

        List<NotificationResponse> notificationResponse = new ArrayList<>();
        prepareNotificationResponse(notificationResponse, userOptional.get());


        String s = toJson(notificationResponse);
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
