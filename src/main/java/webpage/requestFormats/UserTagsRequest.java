package webpage.requestFormats;

import webpage.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class UserTagsRequest {


    private ArrayList<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}


