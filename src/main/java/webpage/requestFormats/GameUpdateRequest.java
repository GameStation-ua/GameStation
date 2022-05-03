package webpage.requestFormats;

public class GameUpdateRequest {

    private final Long gameId;
    private final String title;
    private final String content;
    private final String path;

    public GameUpdateRequest(Long gameId, String title, String content, String path) {
        this.gameId = gameId;
        this.title = title;
        this.content = content;
        this.path = path;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPath() {
        return path;
    }
}