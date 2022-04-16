package webpage.responseFormats;

import webpage.entity.AvailableTag;

import java.util.List;

public class AvailableTagsResponse {
    private final List<AvailableTag> availableTags;

    public AvailableTagsResponse(List<AvailableTag> availableTags) {
        this.availableTags = availableTags;
    }
}
