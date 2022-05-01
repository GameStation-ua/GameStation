package webpage.responseFormats;

import webpage.model.Notification;

import java.util.Date;

public class NotificationResponse {

    private final String path;
    private final Date date;
    private final String content;

    public NotificationResponse(Notification notification) {
        this.path = notification.getPath();
        this.date = notification.getDate();
        this.content = notification.getContent();
    }
}
