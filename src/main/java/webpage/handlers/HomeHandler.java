package webpage.handlers;


import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.model.Game;
import webpage.model.Tag;
import webpage.model.User;
import webpage.responseFormats.GameResponse;
import webpage.responseFormats.HomeResponse;
import webpage.responseFormats.TagResponse;
import webpage.responseFormats.UserResponse;
import webpage.util.HandlerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static spark.Spark.get;
import static webpage.entity.Games.findGameByTagName;
import static webpage.entity.Users.findUserById;
import static webpage.entity.Users.getIdByToken;
import static webpage.util.EntityManagers.createEntityManager;
import static webpage.util.SecretKey.key;

public class HomeHandler extends AbstractHandler{


    public void handle(){

        get("/home", (req, res) -> {
            String token = req.headers("token");
            if (!verifyJWT(token)) {
                res.status(401);
                return "{\"message\":\"Not logged in.\"}";
            }
            Long userId = getIdByToken(token);
            Optional<User> user = findUserById(userId);
            if (user.isEmpty()){
                res.status(400);
                return "{\"message\":\"Something went wrong.\"}";
            }

            UserResponse userResponse = new UserResponse(user.get());            // User ready

            List<Tag> tags = new ArrayList<>(user.get().getLikedTags());
            fillTags(tags);// Tags ready

            Optional<List<Game>> gamesTag1 = findGameByTagName(tags.get(0));
            Optional<List<Game>> gamesTag2 = findGameByTagName(tags.get(1));
            Optional<List<Game>> gamesTag3 = findGameByTagName(tags.get(2));
            Optional<List<Game>> gamesTag4 = findGameByTagName(tags.get(3));
            Optional<List<Game>> gamesTag5 = findGameByTagName(tags.get(4));

            if (gamesTag1.isEmpty() || gamesTag2.isEmpty() || gamesTag3.isEmpty() || gamesTag4.isEmpty() || gamesTag5.isEmpty()){
                res.status(500);
                return "{\"message\":\"Something went wrong.\"}";
            }

            List<GameResponse> gamesForResponse1 = gameForResponseList(gamesTag1.get());
            List<GameResponse> gamesForResponse2 = gameForResponseList(gamesTag2.get());
            List<GameResponse> gamesForResponse3 = gameForResponseList(gamesTag3.get());
            List<GameResponse> gamesForResponse4 = gameForResponseList(gamesTag4.get());
            List<GameResponse> gamesForResponse5 = gameForResponseList(gamesTag5.get());
            List<TagResponse> tagResponseList = tagForResponseList(tags);
            HomeResponse homeResponse = new HomeResponse(userResponse, tagResponseList, gamesForResponse1, gamesForResponse2, gamesForResponse3, gamesForResponse4, gamesForResponse5);
            res.status(200);
            return new Gson().toJson(homeResponse);
        });

        get("/isAdmin", (req, res) -> {
            String token = req.headers("token");
            if (!verifyJWT(token)) {
                res.status(401);
                return "{\"message\":\"Not logged in.\"}";
            }
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                Boolean isAdmin = (Boolean) claims.get("isAdmin");
                res.status(200);
                return "{\"message\":\"" + isAdmin + "\"}";
        });
    }

    private void fillTags(List<Tag> tags) {
        if (tags.size() < 5){
            @SuppressWarnings("unchecked") List<String> availableTags = (List<String>) createEntityManager().createQuery("SELECT availableTag FROM AvailableTag a")
                    .getResultList();
            for (String availableTag : availableTags) {
                boolean isContained = false;
                for (Tag tag : tags) {
                    if (tag.getName().equals(availableTag)) {
                        isContained = true;
                        break;
                    }
                }
                if (!isContained) tags.add(new Tag(availableTag));
                if (tags.size() == 5) break;
            }
        }
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

    private  List<TagResponse> tagForResponseList(List<Tag> tags){
        List<TagResponse> tagResponseList = new ArrayList<>();
        for (Tag tag : tags) {
            tagResponseList.add(new TagResponse(tag));
        }
        return tagResponseList;
    }
}

