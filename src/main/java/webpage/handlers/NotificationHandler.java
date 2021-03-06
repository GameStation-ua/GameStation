package webpage.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webpage.model.Notification;
import webpage.model.User;
import webpage.responseFormats.NotificationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static webpage.api.MailSender.sendMail;
import static webpage.entity.Notifications.prepareNotificationResponse;
import static webpage.entity.Users.findUserById;
import static webpage.entity.Users.getIdByToken;
import static webpage.util.Parser.toJson;


@WebSocket
public class NotificationHandler {

    private static final BiMap<Long, Session> sessionMap = Maps.synchronizedBiMap(HashBiMap.create());

    @OnWebSocketConnect
    public void onConnect(Session user){
        user.setIdleTimeout(200000);
    }

    @OnWebSocketMessage
    public void onMessege(Session user, String text){
        try {
            if(checkRegistered(user, text)) {
                sendNotifications(user, text);
                user.setIdleTimeout(3000000);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void sendnotificationToSocketAndMail(Notification notification, Set<User> users){
        for (User user : users) {
            try {
                sessionMap.get(user.getId()).getRemote().sendString(toJson(new NotificationResponse(notification)));
            } catch (Exception ignored) {
            }
        }
        sendMail(notification, users);
    }


    public static void sendNotificationToSocketAndMail(Long userId, Notification notification) {
            Session session = sessionMap.get(userId);
            try {
                session.getRemote().sendString(toJson(new NotificationResponse(notification)));
            }catch (Exception ignored){
            }
            Optional<User> user = findUserById(userId);
            if (user.isPresent()) {
                sendMail(notification, user.get());
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

    private boolean checkRegistered(Session user, String mesasge) {
        if (!sessionMap.containsValue(user)) {
            try {
                sessionMap.put(getIdByToken(mesasge), user);
                return true;
            }catch (Exception e){
                onError(user, e);
                return false;
            }
        }
        return true;
    }


    @OnWebSocketError
    public void onError(Session user, Throwable error){
        try {
            user.getRemote().sendString(error.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
