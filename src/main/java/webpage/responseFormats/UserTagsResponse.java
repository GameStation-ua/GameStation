package webpage.responseFormats;

import webpage.model.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserTagsResponse {
    List<String> userTags = new ArrayList<>();

    public UserTagsResponse(Set<Tag> usersTags) {
        for (Tag userTags : usersTags) {
            this.userTags.add(userTags.getName());
        }
    }
}
