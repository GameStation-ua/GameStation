package webpage.responseFormats;

import java.util.List;

public class SearchTagResponse {
    private final List<GameForResponse> gameList;

    public SearchTagResponse(List<GameForResponse> gameList) {
        this.gameList = gameList;
    }
}
