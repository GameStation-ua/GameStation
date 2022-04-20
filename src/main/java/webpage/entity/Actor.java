package webpage.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Set;

public interface Actor {

    boolean sendNotification(Notification notification, EntityManager em);

    Set<User> getFollowers();

    String getName();
}