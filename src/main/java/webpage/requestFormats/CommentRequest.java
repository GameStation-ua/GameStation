package webpage.requestFormats;

import webpage.util.NotificationType;

public class CommentRequest {
    private final String content;
    private final NotificationType notificationType;
    private final String path;

    public CommentRequest(String content, NotificationType notificationType, String path) {
        this.content = content;
        this.notificationType = notificationType;
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getPath() {
        return path;
    }
}
