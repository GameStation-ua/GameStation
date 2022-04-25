package webpage.responseFormats;

import webpage.entity.GameUpdate;

import java.util.List;
import java.util.Set;

public class HardGameForResponse {
    private final long gameId;
    private final float meanScore;
    private final int numberOfFollowers;
    private final String title;
    private final String description;
    private final int imgsInCarousel;
    private final String wiki;
    private final List<UserResponse> creators;
    private final List<TagResponse> tags;
    private final Set<GameUpdate> gameUpdates;

    public HardGameForResponse(long gameId, float meanScore, int numberOfFollowers, String title, String description, int imgsInCarousel, String wiki, List<UserResponse> creators, List<TagResponse> tags, Set<GameUpdate> gameUpdates) {
        this.gameId = gameId;
        this.meanScore = meanScore;
        this.numberOfFollowers = numberOfFollowers;
        this.title = title;
        this.description = description;
        this.imgsInCarousel = imgsInCarousel;
        this.wiki = wiki;
        this.creators = creators;
        this.tags = tags;
        this.gameUpdates = gameUpdates;
    }
}
