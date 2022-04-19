package webpage.requestFormats;

import webpage.entity.Status;

public class GameListRequest {
    private final long gameId;
    private final int score;
    private final Status status;

    public GameListRequest(long gameId, int score, Status status) {
        this.gameId = gameId;
        this.score = score;
        this.status = status;
    }

    public long getGameId() {
        return gameId;
    }

    public int getScore() {
        return score;
    }

    public Status getStatus() {
        return status;
    }
}
