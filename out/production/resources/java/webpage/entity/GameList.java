package webpage.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GameList {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(name = "id", nullable = false)
     private Long id;

     @ElementCollection private List<Integer> gameIDs = new ArrayList<>();
     @ElementCollection private List<Integer> scores = new ArrayList<>();
     @ElementCollection private List<Status> statuses = new ArrayList<>();
     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "USER_ID")
     private User user;


     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public User getUser() {
          return user;
     }

     public void setUser(User user) {
          this.user = user;
     }
}
