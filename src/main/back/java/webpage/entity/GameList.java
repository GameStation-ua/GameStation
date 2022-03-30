package webpage.entity;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Embeddable
public class GameList {
     @ElementCollection private List<Integer> gameIDs;
     @ElementCollection
     public List<Integer> scores;
     @ElementCollection private List<Status> statuses;

     public void addGame(Integer gameID, Integer score, Status status){
          gameIDs.add(gameID);
          scores.add(score);
          statuses.add(status);
     }
     public void edit(Integer gameID, Integer score, Status status){
          int index = gameIDs.indexOf(gameID);
          scores.set(index,score);
          statuses.set(index, status);
     }
}
