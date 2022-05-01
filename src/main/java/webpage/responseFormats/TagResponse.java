package webpage.responseFormats;

import webpage.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagResponse {
    private final String name;

    public TagResponse(Tag tag) {
        this.name = tag.getName();
    }

    public static List<TagResponse> createTagResponseList(List<Tag> tagList){
        List<TagResponse> tagsForResponse = new ArrayList<>();
        for (Tag tag : tagList) {
            tagsForResponse.add(new TagResponse(tag));
        }
        return tagsForResponse;
    }
}
