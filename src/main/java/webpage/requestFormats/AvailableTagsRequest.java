package webpage.requestFormats;

import webpage.entity.AvailableTag;
import webpage.entity.Tag;

import java.util.ArrayList;

public class AvailableTagsRequest {

    private ArrayList<AvailableTag> tags;

    public ArrayList<AvailableTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<AvailableTag> tags) {
        this.tags = tags;
    }
}



