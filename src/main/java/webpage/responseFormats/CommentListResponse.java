package webpage.responseFormats;

import java.util.List;

public class CommentListResponse {

    private final List<CommentForResponse> comments;

    public CommentListResponse(List<CommentForResponse> comments) {
        this.comments = comments;
    }

    public List<CommentForResponse> getComments() {
        return comments;
    }
}
