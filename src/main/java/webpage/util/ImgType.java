package webpage.util;

import com.google.gson.annotations.SerializedName;

public enum ImgType {

    @SerializedName(value = "main")
    MAIN,

    @SerializedName(value = "carousel")
    CAROUSEL,

    @SerializedName(value = "gameUpdate")
    GAME_UPDATE
}
