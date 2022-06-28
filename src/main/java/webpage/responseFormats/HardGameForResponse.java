package webpage.responseFormats;

import java.util.List;

import static webpage.entity.Actors.findIfFollowing;

public class HardGameForResponse {
    private final long gameId;
    private final float meanScore;
    private final int numberOfFollowers;
    private final String title;
    private final String description;
    private final int imgsInCarousel;
    private final String wiki;
    private final UserResponse creators;
    private final List<String> tags;
    private boolean isFollowing;

    public HardGameForResponse(long gameId, float meanScore, int numberOfFollowers, String title, String description, int imgsInCarousel, String wiki, UserResponse creators, List<String> tags, Long userId) {
        this.gameId = gameId;
        this.meanScore = meanScore;
        this.numberOfFollowers = numberOfFollowers;
        this.title = title;
        this.description = description;
        this.imgsInCarousel = imgsInCarousel;
        this.wiki = wiki;
        this.creators = creators;
        this.tags = tags;
        this.isFollowing = findIfFollowing(gameId, userId);
    }
}
