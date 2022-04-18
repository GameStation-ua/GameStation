package webpage.responseFormats;

import webpage.entity.Game;

import java.util.List;
import java.util.Set;

public class SearchTagResponse {
    private final List<GameForResponse> gameList;

    public SearchTagResponse(List<GameForResponse> gameList) {
        this.gameList = gameList;
    }
}
