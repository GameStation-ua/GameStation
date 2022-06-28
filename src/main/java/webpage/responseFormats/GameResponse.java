package webpage.responseFormats;

import webpage.model.Game;

import static webpage.entity.Actors.findIfFollowing;

public class GameResponse {
    private final long id;
    private final String title;
    private final String description;
    private final int  imgsInCarousel;
    private boolean isFollowing;

    public GameResponse(Game game, Long userId){
        this.id = game.getId();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.imgsInCarousel = game.getImgsInCarousel();
        this.isFollowing = findIfFollowing(game.getId(), userId);
    }
}