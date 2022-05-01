package webpage.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Optional;

import static webpage.util.EntityManagers.createEntityManager;


public class Persister {




    public static <T> void merge(T entity){
        EntityManager em = createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.merge(entity);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
        em.close();
    }

    public static <T> void persist(Optional<T> entity){
        EntityManager em = createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(entity.get());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
        em.close();
    }

    public static <T> void remove(T entity){
        EntityManager em = createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.remove(entity);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
        em.close();
    }
}
