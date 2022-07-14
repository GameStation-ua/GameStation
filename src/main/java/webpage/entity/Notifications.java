package webpage.entity;

import webpage.model.Notification;
import webpage.model.User;
import webpage.responseFormats.NotificationResponse;

import java.util.List;

public class Notifications {


    public static void prepareNotificationResponse(List<NotificationResponse> notificationResponse, User user){
        int i = 1;
        for (Notification notification : user.getNotifications()) {
            notificationResponse.add(new NotificationResponse(notification));
            i++;
            if (i > 30) return;
        }
    }
}
