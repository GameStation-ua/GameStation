package webpage.responseFormats;

import webpage.model.Game;

import java.util.ArrayList;
import java.util.List;

public class SoftGameResponse {

    private final Long gameId;
    private final String title;


    public SoftGameResponse(Game game) {
        this.gameId = game.getId();
        this.title = game.getTitle();
    }
}
