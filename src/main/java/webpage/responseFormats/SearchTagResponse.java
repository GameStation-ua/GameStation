package webpage.responseFormats;

import java.util.List;

public class SearchTagResponse {
    private final List<SoftGameForResponse> gameList;

    public SearchTagResponse(List<SoftGameForResponse> gameList) {
        this.gameList = gameList;
    }
}
