package webpage.responseFormats;

import webpage.model.Thread;
import webpage.model.User;

import java.util.Date;

import static webpage.entity.Actors.findIfFollowing;

public class ThreadResponse {

    private  final Long gameId;
    private final String title;
    private final String description;
    private final Date date;
    private final UserResponse user;
    private boolean isFollowing;


    public ThreadResponse(Thread thread, User user, Long userId) {
        this.title = thread.getName();
        this.description = thread.getDescription();
        this.user = new UserResponse(user, userId);
        this.date = thread.getDate();
        this.gameId = thread.getGameId();
        this.isFollowing = findIfFollowing(thread.getId(), userId);
    }
}
