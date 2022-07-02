package webpage.handlers;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.model.Game;
import webpage.model.Tag;
import webpage.model.User;
import webpage.responseFormats.GameResponse;
import webpage.responseFormats.HomeResponse;
import webpage.responseFormats.SoftGameResponse;
import webpage.responseFormats.UserResponse;
import webpage.util.HandlerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static spark.Spark.get;
import static webpage.entity.Games.find5GamesByTagName;
import static webpage.entity.Tags.createTagResponseList;
import static webpage.entity.Tags.findAvailableTags;
import static webpage.entity.Users.findUserById;
import static webpage.entity.Users.getIdByToken;
import static webpage.util.Parser.toJson;
import static webpage.util.SecretKey.key;

public class HomeHandler extends AbstractHandler{


    public void handle(){

        get("/home","application/json", (req, res) -> {
            String token = req.headers("token");
            if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
            Long userId = getIdByToken(token);
            Optional<User> user = findUserById(userId);
            if (user.isEmpty()) return returnJson(res, 400, "Something went wrong");

            UserResponse userResponse = new UserResponse(user.get(), userId);            // User ready

            List<Tag> tags = new ArrayList<>(user.get().getLikedTags());

            if(!fillTags(tags)) return returnJson(res, 500, "Something went wrong");

            Optional<List<Game>> gamesTag1 = find5GamesByTagName(tags.get(0));
            Optional<List<Game>> gamesTag2 = find5GamesByTagName(tags.get(1));
            Optional<List<Game>> gamesTag3 = find5GamesByTagName(tags.get(2));
            Optional<List<Game>> gamesTag4 = find5GamesByTagName(tags.get(3));
            Optional<List<Game>> gamesTag5 = find5GamesByTagName(tags.get(4));

            if (gamesTag1.isEmpty() || gamesTag2.isEmpty() || gamesTag3.isEmpty() || gamesTag4.isEmpty() || gamesTag5.isEmpty()){
                return returnJson(res, 500, "Something went wrong");
            }

            List<SoftGameResponse> gamesForResponse1 = gameForResponseList(gamesTag1.get(), userId);
            List<SoftGameResponse> gamesForResponse2 = gameForResponseList(gamesTag2.get(), userId);
            List<SoftGameResponse> gamesForResponse3 = gameForResponseList(gamesTag3.get(), userId);
            List<SoftGameResponse> gamesForResponse4 = gameForResponseList(gamesTag4.get(), userId);
            List<SoftGameResponse> gamesForResponse5 = gameForResponseList(gamesTag5.get(), userId);
            List<String> tagResponseList = createTagResponseList(tags);
            HomeResponse homeResponse = new HomeResponse(userResponse, tagResponseList, gamesForResponse1, gamesForResponse2, gamesForResponse3, gamesForResponse4, gamesForResponse5);
            res.status(200);
            return  toJson(homeResponse);
        });

        get("/isAdmin","application/json", (req, res) -> {
            String token = req.headers("token");
            if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
            Boolean isAdmin = (Boolean) claims.get("isAdmin");
            return returnJson(res, 200, "" + isAdmin + "");
        });
    }

    private boolean fillTags(List<Tag> tags) {
        if (tags.size() < 5){
            Optional<List<Tag>> availableTags = findAvailableTags();
            if (availableTags.isEmpty()) return false;
            for (Tag availableTag : availableTags.get()) {
                boolean isContained = false;
                for (Tag tag : tags) {
                    if (availableTag.equals(tag)) {
                        isContained = true;
                        break;
                    }
                }
                if (!isContained) tags.add(availableTag);
                if (tags.size() == 5) break;
            }
        }
        return true;
    }

    @Override
    public HandlerType getType() {
        return HandlerType.HOME;
    }

    private List<SoftGameResponse> gameForResponseList(List<Game> games, Long userId){
        List<SoftGameResponse> gamesForResponse = new ArrayList<>();
        for (Game game : games) {
            gamesForResponse.add(new SoftGameResponse(game, userId));
        }
        return gamesForResponse;
    }
}

