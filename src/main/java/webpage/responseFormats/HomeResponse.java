package webpage.responseFormats;

import java.util.List;

public class HomeResponse {
    private final UserResponse user;
    private final List<TagResponse> userLikedTags;
    private final List<GameResponse> gamesTag1;
    private final List<GameResponse> gamesTag2;
    private final List<GameResponse> gamesTag3;
    private final List<GameResponse> gamesTag4;
    private final List<GameResponse> gamesTag5;

    public HomeResponse(UserResponse user, List<TagResponse> userLikedTags, List<GameResponse> gamesTag1, List<GameResponse> gamesTag2, List<GameResponse> gamesTag3, List<GameResponse> gamesTag4, List<GameResponse> gamesTag5) {
        this.user = user;
        this.userLikedTags = userLikedTags;
        this.gamesTag1 = gamesTag1;
        this.gamesTag2 = gamesTag2;
        this.gamesTag3 = gamesTag3;
        this.gamesTag4 = gamesTag4;
        this.gamesTag5 = gamesTag5;
    }
}
