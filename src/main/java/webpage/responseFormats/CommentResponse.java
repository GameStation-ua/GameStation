package webpage.responseFormats;

import webpage.model.Comment;

import java.util.Date;

public class CommentResponse {
    private final Long id;
    private final Long userId;
    private final String nickname;
    private final Integer userVote;
    private final Date date = new Date();
    private final String content;
    private final int votes;

    public CommentResponse(Comment comment, String nickname, Integer vote) {
        this.userVote = vote;
        this.id = comment.getId();
        this.nickname = nickname;
        this.userId = comment.getUserId();
        this.content = comment.getContent();
        this.votes = comment.getVotes();
    }
}
