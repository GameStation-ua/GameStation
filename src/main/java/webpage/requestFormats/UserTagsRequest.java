package webpage.requestFormats;

import webpage.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class UserTagsRequest {


    private ArrayList<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}


