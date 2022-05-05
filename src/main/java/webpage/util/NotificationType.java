package webpage.util;

import com.google.gson.annotations.SerializedName;

public enum NotificationType {
    @SerializedName("user commented on profile")
    USER_COMMENTED_ON_PROFILE,
    @SerializedName("user started following")
    USER_STARTED_FOLLOWING,
    @SerializedName("user comment on user thread")
    USER_COMMENTED_ON_USER_THREAD,
    @SerializedName("followed user comments")
    FOLLOWED_USER_COMMENTS,
    @SerializedName("game posted update")
    GAME_POSTED_UPDATE,
    @SerializedName("user commented on followed thread")
    USER_COMMENTED_ON_FOLLOWED_THREAD
}
