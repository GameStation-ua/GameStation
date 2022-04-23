package webpage.responseFormats;

import java.util.List;

public class SearchResponse {
    private final List<SoftGameForResponse> foundGames;
    private final List<UserForResponse> foundUsers;

    public SearchResponse(List<SoftGameForResponse> foundGames, List<UserForResponse> foundUsers) {
        this.foundGames = foundGames;
        this.foundUsers = foundUsers;
    }
}
