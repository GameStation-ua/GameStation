package webpage.entity;

import com.google.gson.annotations.SerializedName;

public enum Status {
    @SerializedName("Playing")
    PLAYING,
    @SerializedName("Dropped")
    DROPPED,
    @SerializedName("On hold")
    ON_HOLD,
    @SerializedName("Waiting")
    WAITING,
    @SerializedName("Completed")
    COMPLETED
}
