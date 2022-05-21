package webpage.requestFormats;

import java.util.List;

public class CreateGameRequest {
    private final String title;
    private final String description;
    private final List<String> tags;
    private final String wiki;


    public CreateGameRequest(String title, String description, List<String> tags, String wiki) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.wiki = wiki;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getWiki() {
        return wiki;
    }
}
