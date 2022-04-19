package webpage.entity;

import com.google.gson.annotations.SerializedName;

public enum EntityType {
    @SerializedName("Game")
    GAME,
    @SerializedName("Thread")
    THREAD,
    @SerializedName("User")
    USER
}
