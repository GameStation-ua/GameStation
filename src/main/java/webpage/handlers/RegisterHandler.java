package webpage.handlers;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import webpage.entity.User;

import javax.persistence.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static spark.Spark.*;

public class RegisterHandler extends AbstractHandler {

    public RegisterHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle(String path) throws IOException {
        enableCORS();
        post(path, "application/json", (req, res) -> {
            User user1 = new Gson().fromJson(req.body(), User.class);
            final EntityManager em = emf.createEntityManager();
            Query query = em.createQuery("FROM User user WHERE user.username = :username");
            query = query.setParameter("username", user1.getUsername());
            List results = query.getResultList();
            if (results.isEmpty()) {
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
                return "{\"message\":\"User created!\"}";
                }
            }else {
                res.type("application/json");
                res.status(200);
                return "{\"message\":\"Username already taken.\"}";
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
