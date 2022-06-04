package webpage.responseFormats;

import webpage.model.GameRequest;
import webpage.model.Tag;

import java.util.HashSet;
import java.util.Set;

public class GameRequestResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final Set<String> tags;
    private final String wiki;
    private final Integer imgsInCarousel;

    public GameRequestResponse(GameRequest gr) {
        this.id = gr.getId();
        this.title = gr.getTitle();
        this.description = gr.getDescription();
        Set<String> tags = new HashSet<>();
        for (Tag tag : gr.getTags()) {
            tags.add(tag.getName());
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

    public Set<String> getTags() {
        return tags;
    }

    public String getWiki() {
        return wiki;
    }

    public Integer getImgsInCarousel() {
        return imgsInCarousel;
    }
}
