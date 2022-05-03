package webpage.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagers {


  private static EntityManagerFactory emf;

  public static void setFactory(EntityManagerFactory emf) {
    EntityManagers.emf = emf;
  }

  public static EntityManager createEntityManager() {
    return emf.createEntityManager();
  }
}
