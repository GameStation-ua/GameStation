package webpage.responseFormats;

import webpage.entity.Thread;
import webpage.entity.User;

import java.util.Date;

public class ThreadResponse {

    private  final Long gameId;
    private final String title;
    private final String description;
    private final Date date;
    private final UserResponse user;


    public ThreadResponse(Thread thread, User user) {
        this.title = thread.getName();
        this.description = thread.getDescription();
        this.user = new UserResponse(user);
        this.date = thread.getDate();
        this.gameId = thread.getGameId();
    }
}
