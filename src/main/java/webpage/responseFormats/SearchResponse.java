package webpage.responseFormats;

import java.util.List;

public class SearchResponse {
    private final List<SoftGameForResponse> foundGames;
    private final List<UserResponse> foundUsers;

    public SearchResponse(List<SoftGameForResponse> foundGames, List<UserResponse> foundUsers) {
        this.foundGames = foundGames;
        this.foundUsers = foundUsers;
    }
}
