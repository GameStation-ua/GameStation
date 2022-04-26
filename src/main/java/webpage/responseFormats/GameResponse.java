package webpage.responseFormats;

import webpage.entity.Game;

public class GameResponse {
    private final long id;
    private final String title;
    private final String description;
    private final int  imgsInCarousel;

    public GameResponse(Game game){
        this.id = game.getId();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.imgsInCarousel = game.getImgsInCarousel();
    }
}