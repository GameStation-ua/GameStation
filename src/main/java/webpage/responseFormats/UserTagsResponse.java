package webpage.responseFormats;

import webpage.entity.AvailableTag;
import webpage.entity.Tag;

import java.util.List;

public class UserTagsResponse {
    List<Tag> userTags;

    public UserTagsResponse( List<Tag> userTags) {
        this.userTags = userTags;
    }
}
