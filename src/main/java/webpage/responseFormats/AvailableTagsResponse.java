package webpage.responseFormats;

import webpage.model.AvailableTag;

import java.util.List;

public class AvailableTagsResponse {
    private final List<AvailableTag> availableTags;

    public AvailableTagsResponse(List<AvailableTag> availableTags) {
        this.availableTags = availableTags;
    }
}
