package webpage.responseFormats;

import java.util.List;

public class SearchResponse {
    private final List<GameResponse> foundGames;
    private final List<UserResponse> foundUsers;

    public SearchResponse(List<GameResponse> foundGames, List<UserResponse> foundUsers) {
        this.foundGames = foundGames;
        this.foundUsers = foundUsers;
    }
}
