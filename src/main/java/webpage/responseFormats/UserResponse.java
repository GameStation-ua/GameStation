package webpage.responseFormats;

import webpage.model.Tag;
import webpage.model.User;

import java.util.HashSet;
import java.util.Set;

import static webpage.entity.Actors.findIfFollowing;

public class UserResponse {
    private final long id;
    private final String nickname;
    private final Set<String> likedTags;
    private boolean isFollwing;

    public UserResponse(User userToRespond, Long userId){
        this.id = userToRespond.getId();
        this.nickname = userToRespond.getNickname();
        Set<String> tags = new HashSet<>();
        for (Tag likedTag : userToRespond.getLikedTags()) {
            tags.add(likedTag.getName());
        }
        this.likedTags = tags;
        this.isFollwing = findIfFollowing(userToRespond.getId(), userId);
    }
}
