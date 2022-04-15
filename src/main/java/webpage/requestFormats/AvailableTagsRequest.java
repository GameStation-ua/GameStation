package webpage.requestFormats;

import webpage.entity.Tag;

import java.util.ArrayList;

public class AvailableTagsRequest {

    private ArrayList<Tag> tags;

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}



