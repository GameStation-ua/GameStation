package webpage.responseFormats;

import webpage.model.Game;

import static webpage.entity.Actors.findIfFollowing;

public class SoftGameResponse {

    private final Long gameId;
    private final String title;
    private boolean isFollowing;

    public SoftGameResponse(Game game, Long userId) {
        this.gameId = game.getId();
        this.title = game.getTitle();
        this.isFollowing = findIfFollowing(game.getId(), userId);
    }
}
