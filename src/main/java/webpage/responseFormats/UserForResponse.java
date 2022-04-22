package webpage.responseFormats;

import webpage.entity.Tag;
import webpage.entity.User;

import java.util.HashSet;
import java.util.Set;

public class UserForResponse {
    private final long id;
    private final String nickname;
    private final Set<String> likedTags;

    public UserForResponse(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        Set<String> tags = new HashSet<>();
        for (Tag likedTag : user.getLikedTags()) {
            tags.add(likedTag.getName());
        }
        this.likedTags = tags;
    }
}
