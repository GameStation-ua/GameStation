package webpage.responseFormats;

import webpage.entity.Tag;

public class TagResponse {
    private final String name;

    public TagResponse(Tag tag) {
        this.name = tag.getName();
    }
}
