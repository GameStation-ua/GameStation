package webpage.requestFormats;

import java.util.List;

public class EditGameRequest {
        private String title;
        private Long gameId;
        private String description;
        private List<String> tags;
        private String wiki;


    public EditGameRequest(String title, Long gameId, String description, List<String> tags, String wiki) {
        this.title = title;
        this.gameId = gameId;
        this.description = description;
        this.tags = tags;
        this.wiki = wiki;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getTitle() {
            return title;
        }

        public Long getGameId() {
            return gameId;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getTags() {
            return tags;
        }

        public String getWiki() {
            return wiki;
        }
}
