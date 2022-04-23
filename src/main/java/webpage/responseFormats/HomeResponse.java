package webpage.responseFormats;

import java.util.List;

public class HomeResponse {
    private final UserForResponse user;
    private final List<TagForResponse> userLikedTags;
    private final List<SoftGameForResponse> gamesTag1;
    private final List<SoftGameForResponse> gamesTag2;
    private final List<SoftGameForResponse> gamesTag3;
    private final List<SoftGameForResponse> gamesTag4;
    private final List<SoftGameForResponse> gamesTag5;

    public HomeResponse(UserForResponse user, List<TagForResponse> userLikedTags, List<SoftGameForResponse> gamesTag1, List<SoftGameForResponse> gamesTag2, List<SoftGameForResponse> gamesTag3, List<SoftGameForResponse> gamesTag4, List<SoftGameForResponse> gamesTag5) {
        this.user = user;
        this.userLikedTags = userLikedTags;
        this.gamesTag1 = gamesTag1;
        this.gamesTag2 = gamesTag2;
        this.gamesTag3 = gamesTag3;
        this.gamesTag4 = gamesTag4;
        this.gamesTag5 = gamesTag5;
    }
}
