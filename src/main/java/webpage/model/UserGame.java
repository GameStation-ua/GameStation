package webpage.model;

import com.google.gson.annotations.SerializedName;
import webpage.util.Status;

import javax.persistence.*;

@Entity
public class UserGame {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "USER_ID", nullable = false)
     private Long userId;

     @Column(name = "GAME_ID", nullable = false)
     private Long gameId;

     @Column(name = "SCORE")
     private Integer score;

     @Column(name = "STATUS")
     @Enumerated(EnumType.STRING)
     @SerializedName("STATUS")
     private Status status;

     public UserGame(Long userId, Status status, int score, long gameId) {
          this.userId = userId;
          this.status = status;
          this.score = score;
          this.gameId = gameId;
     }

     public UserGame() {

     }

     public Long getGameId() {
          return gameId;
     }

     public void setGameId(Long gameId) {
          this.gameId = gameId;
     }

     public Long getUserId() {
          return userId;
     }

     public void setUserId(Long userId) {
          this.userId = userId;
     }

     public Integer getScore() {
          return score;
     }

     public void setScore(Integer score) {
          this.score = score;
     }

     public Status getStatus() {
          return status;
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public void setStatus(Status status) {
          this.status = status;
     }
}
