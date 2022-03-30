package webpage.entity;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class GameList {
     @ElementCollection private List<Integer> gameIDs = new ArrayList<>();
     @ElementCollection private List<Integer> scores = new ArrayList<>();
     @ElementCollection private List<Status> statuses = new ArrayList<>();
}
