package webpage.responseFormats;

import java.util.List;

public class HomeResponse {
    private final UserResponse user;
    private final List<String> userLikedTags;
    private final List<SoftGameResponse> gamesTag1;
    private final List<SoftGameResponse> gamesTag2;
    private final List<SoftGameResponse> gamesTag3;
    private final List<SoftGameResponse> gamesTag4;
    private final List<SoftGameResponse> gamesTag5;

    public HomeResponse(UserResponse user, List<String> userLikedTags, List<SoftGameResponse> gamesTag1, List<SoftGameResponse> gamesTag2, List<SoftGameResponse> gamesTag3, List<SoftGameResponse> gamesTag4, List<SoftGameResponse> gamesTag5) {
        this.user = user;
        this.userLikedTags = userLikedTags;
        this.gamesTag1 = gamesTag1;
        this.gamesTag2 = gamesTag2;
        this.gamesTag3 = gamesTag3;
        this.gamesTag4 = gamesTag4;
        this.gamesTag5 = gamesTag5;
    }
}
