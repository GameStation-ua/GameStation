package webpage.responseFormats;

import webpage.model.GameRequest;
import webpage.model.Tag;

import java.util.HashSet;
import java.util.Set;

public class GameRequestResponse {

    private final String title;
    private final String description;
    private final Set<TagResponse> tags;
    private final String wiki;
    private final Integer imgsInCarousel;

    public GameRequestResponse(GameRequest gr) {
        this.title = gr.getTitle();
        this.description = gr.getDescription();
        Set<TagResponse> tags = new HashSet<>();
        for (Tag tag : gr.getTags()) {
            tags.add(new TagResponse(tag));
        }
        this.tags = tags;
        this.wiki = gr.getWiki();
        this.imgsInCarousel = gr.getImgsInCarousel();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Set<TagResponse> getTags() {
        return tags;
    }

    public String getWiki() {
        return wiki;
    }

    public Integer getImgsInCarousel() {
        return imgsInCarousel;
    }
}
