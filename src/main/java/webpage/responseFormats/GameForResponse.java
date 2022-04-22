package webpage.responseFormats;

import webpage.entity.Game;

public class GameForResponse {
    private final long id;
    private final String title;
    private final String description;
    private final int  imgsInCarousel;

    public GameForResponse(Game game){
        this.id = game.getId();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.imgsInCarousel = game.getImgsInCarousel();
    }
}
