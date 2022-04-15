package webpage.responseFormats;

import webpage.entity.AvailableTag;
import webpage.entity.Tag;

import java.util.List;

public class UserTagsResponse {
    List<AvailableTag> availableTags;
    List<Tag> userTags;

    public UserTagsResponse(List<AvailableTag> availableTags, List<Tag> userTags) {
        this.availableTags = availableTags;
        this.userTags = userTags;
    }
}
