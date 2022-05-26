package webpage.responseFormats;

import java.util.List;

public class SearchResponse {
    private final List<SoftGameResponse> foundGames;
    private final List<UserResponse> foundUsers;

    public SearchResponse(List<SoftGameResponse> foundGames, List<UserResponse> foundUsers) {
        this.foundGames = foundGames;
        this.foundUsers = foundUsers;
    }
}
