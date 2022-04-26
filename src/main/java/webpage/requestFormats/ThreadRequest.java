package webpage.requestFormats;

public class ThreadRequest {

    private final Long gameId;
    private final String description;
    private final String title;

    public ThreadRequest(Long gameId, String description, String title) {
        this.gameId = gameId;
        this.description = description;
        this.title = title;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
