package webpage.requestFormats;

import webpage.entity.Tag;

import java.util.ArrayList;

public class AvailableTagsRequest {

    private String token;
    private ArrayList<Tag> tags;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}



