package webpage.responseFormats;

import webpage.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class AvailableTagsResponse {
    private final List<String> availableTags;

    public AvailableTagsResponse(List<Tag> availableTags) {
        List<String> response = new ArrayList<>();
        for (Tag availableTag : availableTags) {
            response.add(availableTag.getName());
        }
        this.availableTags = response;
    }
}
