package webpage.responseFormats;

import webpage.model.Tag;
import webpage.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserResponse {
    private final long id;
    private final String nickname;
    private final Set<String> likedTags;

    public UserResponse(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        Set<String> tags = new HashSet<>();
        for (Tag likedTag : user.getLikedTags()) {
            tags.add(likedTag.getName());
        }
        this.likedTags = tags;
    }
}
