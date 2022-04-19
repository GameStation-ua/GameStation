package webpage.responseFormats;

import java.util.List;

public class HomeResponse {
    private final UserForResponse user;
    private final List<TagForResponse> userLikedTags;
    private final List<GameForResponse> gamesTag1;
    private final List<GameForResponse> gamesTag2;
    private final List<GameForResponse> gamesTag3;
    private final List<GameForResponse> gamesTag4;
    private final List<GameForResponse> gamesTag5;

    public HomeResponse(UserForResponse user, List<TagForResponse> userLikedTags, List<GameForResponse> gamesTag1, List<GameForResponse> gamesTag2, List<GameForResponse> gamesTag3, List<GameForResponse> gamesTag4, List<GameForResponse> gamesTag5) {
        this.user = user;
        this.userLikedTags = userLikedTags;
        this.gamesTag1 = gamesTag1;
        this.gamesTag2 = gamesTag2;
        this.gamesTag3 = gamesTag3;
        this.gamesTag4 = gamesTag4;
        this.gamesTag5 = gamesTag5;
    }
}
