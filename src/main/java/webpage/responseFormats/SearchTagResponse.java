package webpage.responseFormats;

import java.util.List;

public class SearchTagResponse {
    private final List<GameResponse> gameList;

    public SearchTagResponse(List<GameResponse> gameList) {
        this.gameList = gameList;
    }
}
