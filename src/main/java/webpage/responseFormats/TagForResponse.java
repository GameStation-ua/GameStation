package webpage.responseFormats;

import webpage.entity.Tag;

public class TagForResponse {
    private final String name;

    public TagForResponse(Tag tag) {
        this.name = tag.getName();
    }
}
