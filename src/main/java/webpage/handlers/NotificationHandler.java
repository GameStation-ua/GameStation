package webpage.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import webpage.model.Notification;
import webpage.model.User;
import webpage.responseFormats.NotificationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static webpage.entity.Notifications.prepareNotificationResponse;
import static webpage.entity.Users.findUserById;
import static webpage.entity.Users.getIdByToken;
import static webpage.util.Parser.toJson;
import static webpage.util.SecretKey.key;


@WebSocket
public class NotificationHandler {

    private static final BiMap<Long, Session> sessionMap = Maps.synchronizedBiMap(HashBiMap.create());

    @OnWebSocketConnect
    public void onConnect(Session user){
    }

    @OnWebSocketMessage
    public void onMessege(Session user, String text){
        try {
            checkRegistered(user, text);
            sendNotifications(user, text);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static public void sendNotification(Long userId, Notification notification){
        Session session = sessionMap.get(userId);
        try {
            session.getRemote().sendString(toJson(new NotificationResponse(notification)));
        }catch (Exception ignored){
        }
    }

    @OnWebSocketClose
    public void onClose(Session user, int statuCode, String reason){
        sessionMap.inverse().remove(user);
    }

    private void sendNotifications(Session user, String message) {
        try{
            Long userId = getIdByToken(message);
            Optional<User> userOptional = findUserById(userId);

        if (userOptional.isEmpty()){
            onError(user, new Exception("User not found"));
        }

        List<NotificationResponse> notificationResponse = new ArrayList<>();
        if (userOptional.isEmpty()) {
            onError(user, new Error("Server error"));
            return;
        }
        prepareNotificationResponse(notificationResponse, userOptional.get());

        String s = toJson(notificationResponse);
        user.getRemote().sendString(s);
        }catch (Throwable e){
            onError(user, e);
        }
    }

    private void checkRegistered(Session user, String mesasge) {
        if (!sessionMap.containsValue(user)) {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(mesasge).getBody();
            sessionMap.put(Long.valueOf((Integer)claims.get("id")), user);
        }
    }


    @OnWebSocketError
    public void onError(Session user, Throwable error){
        user.getUpgradeResponse().setHeader("error", error.getMessage());
    }
}
