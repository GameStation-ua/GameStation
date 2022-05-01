package webpage.responseFormats;

import webpage.model.Game;

import java.util.ArrayList;
import java.util.List;

public class SoftGameResponse {

    private final Long gameId;
    private final String title;


    public SoftGameResponse(Game game) {
        this.gameId = game.getId();
        this.title = game.getTitle();
    }

    public static List<SoftGameResponse> createSoftGameResponseList(List<Game> games){
        List<SoftGameResponse> softGameResponseList = new ArrayList<>();
        for (Game game : games) {
            softGameResponseList.add(new SoftGameResponse(game));
        }
        return softGameResponseList;
    }
}
