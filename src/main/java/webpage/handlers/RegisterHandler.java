package webpage.handlers;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import webpage.entity.User;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;

import static spark.Spark.post;


public class RegisterHandler extends AbstractHandler {

    public RegisterHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle(String path){
        enableCORS();
        post(path, "application/json", (req, res) -> {
            User user1 = new Gson().fromJson(req.body(), User.class);
            if (user1.getPassword() == null || user1.getUsername() == null || user1.getNickname() == null){
                return "{\"message\":\"You need to fill all the fields\"}";
            }
            final EntityManager em = emf.createEntityManager();
            Query query = em.createQuery("FROM User user WHERE user.username = :username");
            query = query.setParameter("username", user1.getUsername());
            try {
                Object object = query.getSingleResult();
                res.type("application/json");
                res.status(200);
                return "{\"message\":\"Username already taken.\"}";
            } catch(NoResultException e){
                if (!(checkString(user1.getPassword())) || (user1.getPassword().length() < 8)){
                    res.type("application/json");
                    res.status(200);
                    return "{\"message\":\"You need to meet password requirements.\"}";
                }else{
                    res.type("application/json");
                    res.status(201);
                    user1.setPassword(Hashing.sha512().hashString(user1.getPassword(), StandardCharsets.UTF_8).toString());
                    EntityTransaction transaction = em.getTransaction();
                    transaction.begin();
                    em.persist(user1);
                    transaction.commit();
                    em.close();
                    return "{\"message\":\"User created!\"}";
                }
            }
        });
    }

    private static boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }
}
