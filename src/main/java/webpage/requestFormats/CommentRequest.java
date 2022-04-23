package webpage.requestFormats;

public class CommentRequest {
    private final String content;

    public CommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
