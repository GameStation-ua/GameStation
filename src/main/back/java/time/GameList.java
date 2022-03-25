package time;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
public class GameList {
     @ElementCollection private List<Integer> gameIDs;
     @ElementCollection private List<Integer> scores;
     @ElementCollection private List<Status> statuses;
}
