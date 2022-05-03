package webpage.requestFormats;

import webpage.model.Tag;

import java.util.List;

public class CreateGameRequest {
    private final String title;
    private final String description;
    private final List<Tag> tags;
    private final String wiki;
    private final String mainImguuid;
    private final List<String> carouseluuids;


    public CreateGameRequest(String title, String description, List<Tag> tags, String wiki, String mainImguuid, List<String> carouseluuids) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.wiki = wiki;
        this.mainImguuid = mainImguuid;
        this.carouseluuids = carouseluuids;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getWiki() {
        return wiki;
    }

    public String getMainImguuid() {
        return mainImguuid;
    }

    public List<String> getCarouseluuids() {
        return carouseluuids;
    }
}
