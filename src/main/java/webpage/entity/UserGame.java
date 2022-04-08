package webpage.entity;

import javax.persistence.*;

@Entity
public class UserGame {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name = "id", nullable = false)
     private Long id;

     @Column(name = "USER_ID")
     private Long userId;

     @Column(name = "GAME_ID")
     private Long gameId;

     @Column(name = "SCORE")
     private Integer scores;

     @Column(name = "STATUS")
     private Status status;


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

     public Status getStatus() {
          return status;
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

}
