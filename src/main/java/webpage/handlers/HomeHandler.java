package webpage.handlers;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.model.Game;
import webpage.model.Tag;
import webpage.model.User;
import webpage.responseFormats.GameResponse;
import webpage.responseFormats.HomeResponse;
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

        get("/home", (req, res) -> {
            String token = req.headers("token");
            if (!verifyJWT(token)) return returnMessage(res, 401, "Not logged in");
            Long userId = getIdByToken(token);
            Optional<User> user = findUserById(userId);
            if (user.isEmpty()) return returnMessage(res, 400, "Something went wrong");

            UserResponse userResponse = new UserResponse(user.get());            // User ready

            List<Tag> tags = new ArrayList<>(user.get().getLikedTags());

            if(!fillTags(tags)) return returnMessage(res, 500, "Something went wrong");

            Optional<List<Game>> gamesTag1 = find5GamesByTagName(tags.get(0));
            Optional<List<Game>> gamesTag2 = find5GamesByTagName(tags.get(1));
            Optional<List<Game>> gamesTag3 = find5GamesByTagName(tags.get(2));
            Optional<List<Game>> gamesTag4 = find5GamesByTagName(tags.get(3));
            Optional<List<Game>> gamesTag5 = find5GamesByTagName(tags.get(4));

            if (gamesTag1.isEmpty() || gamesTag2.isEmpty() || gamesTag3.isEmpty() || gamesTag4.isEmpty() || gamesTag5.isEmpty()){
                return returnMessage(res, 500, "Something went wrong");
            }

            List<GameResponse> gamesForResponse1 = gameForResponseList(gamesTag1.get());
            List<GameResponse> gamesForResponse2 = gameForResponseList(gamesTag2.get());
            List<GameResponse> gamesForResponse3 = gameForResponseList(gamesTag3.get());
            List<GameResponse> gamesForResponse4 = gameForResponseList(gamesTag4.get());
            List<GameResponse> gamesForResponse5 = gameForResponseList(gamesTag5.get());
            List<String> tagResponseList = createTagResponseList(tags);
            HomeResponse homeResponse = new HomeResponse(userResponse, tagResponseList, gamesForResponse1, gamesForResponse2, gamesForResponse3, gamesForResponse4, gamesForResponse5);
            res.status(200);
            return  toJson(homeResponse);
        });

        get("/isAdmin", (req, res) -> {
            String token = req.headers("token");
            if (!verifyJWT(token)) return returnMessage(res, 401, "Not logged in");
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
            Boolean isAdmin = (Boolean) claims.get("isAdmin");
            return returnMessage(res, 200, "" + isAdmin + "");
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

    private List<GameResponse> gameForResponseList(List<Game> games){
        List<GameResponse> gamesForResponse = new ArrayList<>();
        for (Game game : games) {
            gamesForResponse.add(new GameResponse(game));
        }
        return gamesForResponse;
    }
}

