package webpage.entity;

import webpage.model.Notification;
import webpage.model.User;
import webpage.responseFormats.NotificationResponse;
import static webpage.util.EntityManagers.close;
import java.util.List;

public class Notifications {



    public static void prepareNotificationResponse(List<NotificationResponse> notificationResponse, User user){
        int i = 0;
        for (Notification notification : user.getNotifications()) {
            notificationResponse.add(new NotificationResponse(notification));
            i++;
            if (i > 30) break;
        }
    }
}
