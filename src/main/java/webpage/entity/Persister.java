package webpage.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static webpage.util.EntityManagers.createEntityManager;


public class Persister {




    public static <T> T merge(T entity){
        EntityManager em = createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            T t = em.merge(entity);

            tx.commit();
            return t;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    public static <T> void persist(T entity){
        EntityManager em = createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(entity);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    public static <T> void remove(T entity){
        EntityManager em = createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.remove(em.contains(entity) ? entity : em.merge(entity));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }finally {
            em.close();
        }
    }
}
