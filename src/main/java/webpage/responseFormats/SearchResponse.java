package webpage.responseFormats;

import java.util.List;

public class SearchResponse {
    private final List<GameForResponse> foundGames;
    private final List<UserForResponse> foundUsers;

    public SearchResponse(List<GameForResponse> foundGames, List<UserForResponse> foundUsers) {
        this.foundGames = foundGames;
        this.foundUsers = foundUsers;
    }
}
