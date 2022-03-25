package util;

import javax.persistence.EntityTransaction;
import java.util.function.Supplier;

import static util.EntityManagers.currentEntityManager;

public class Transactions {

  private Transactions() {}

  public static <R> R tx(Supplier<R> s) {
    final EntityTransaction tx = currentEntityManager().getTransaction();

    try {
      tx.begin();

      R r = s.get();

      tx.commit();
      return r;
    } catch (Exception e) {
      tx.rollback();
      throw e;
    }
  }

  public static void tx(Runnable r){
    final EntityTransaction tx = currentEntityManager().getTransaction();

    try {
      tx.begin();

      r.run();

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw e;
    }
  }

}
