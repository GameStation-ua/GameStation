package webpage.responseFormats;

import webpage.entity.Tag;
import webpage.entity.User;

import java.util.List;
import java.util.Set;

public class UserForResponse {
    private final int id;
    private final String nickname;
    private final Set<Tag> likedTags;

    public UserForResponse(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.likedTags = user.getLikedTags();
    }
}
