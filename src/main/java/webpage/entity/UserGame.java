package webpage.entity;

import javax.persistence.*;

@Entity
public class UserGame {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column
     private Long userId;

     @Column
     private Long gameId;

     public Long getGameId() {
          return gameId;
     }

     public void setGameId(Long gameId) {
          this.gameId = gameId;
     }

     @Column private Integer scores;
     @Column private Status statuses;

     @ManyToOne
     @JoinColumn(name = "game_id")
     private Game game;

     public Game getGame() {
          return game;
     }

     public void setGame(Game game) {
          this.game = game;
     }

     public Long getUserId() {
          return userId;
     }

     public void setUserId(Long userId) {
          this.userId = userId;
     }

     public Integer getScores() {
          return scores;
     }

     public void setScores(Integer scores) {
          this.scores = scores;
     }

     public Status getStatuses() {
          return statuses;
     }

     public void setStatuses(Status statuses) {
          this.statuses = statuses;
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

}
