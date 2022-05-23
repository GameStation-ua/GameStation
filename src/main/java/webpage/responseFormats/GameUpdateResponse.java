package webpage.responseFormats;

import webpage.model.GameUpdate;

import java.util.Date;

public class GameUpdateResponse {

    private Long id;
    private String title;
    private String content;
    private final Date date;

    public GameUpdateResponse(GameUpdate gameUpdate) {
        this.id = gameUpdate.getId();
        this.title = gameUpdate.getTitle();
        this.content = gameUpdate.getContent();
        this.date = gameUpdate.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }
}
