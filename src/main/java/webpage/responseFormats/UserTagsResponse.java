package webpage.responseFormats;

import webpage.entity.Tag;

import java.util.ArrayList;
import java.util.List;

public class UserTagsResponse {
    List<String> userTags = new ArrayList<>();

    public UserTagsResponse( List<Tag> usersTags) {
        for (Tag userTags : usersTags) {
            this.userTags.add(userTags.getName());
        }
    }
}
