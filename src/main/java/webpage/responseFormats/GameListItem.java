package webpage.responseFormats;

import webpage.model.UserGame;
import webpage.util.Status;

public class GameListItem {

    private final Status status;
    private final int score;
    private final Long gameId;
    private final String title;

    public GameListItem(UserGame userGame, String title) {
        this.status = userGame.getStatus();
        this.score = userGame.getScore();
        this.gameId = userGame.getGameId();
        this.title = title;
    }
}
