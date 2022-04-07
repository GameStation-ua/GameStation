package webpage.handlers;

import com.google.gson.Gson;
import webpage.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.IOException;
import java.util.List;

import static webpage.util.MyValues.*;

import static spark.Spark.*;

public class RegisterHandler extends AbstractHandler{

    public RegisterHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle(EntityManagerFactory emf, String path) throws IOException {
        enableCORS();
        post(path, "application/json", (req, res) -> {
            User user1 = new Gson().fromJson(req.body(), User.class);
            final EntityManager em = this.emf.createEntityManager();
            Query query = em.createQuery("FROM User user WHERE user.userName = " + user1.getUserName());
//            query.setParameter(0, user1.getUserName());
            try {
//                List results = query.getFirstResult(0);
            }catch (IllegalArgumentException e){
                if (user1.getPassword().length() < 8){
                    get(path, "application/json", ((request, response) -> {
                        response.type("application/json");
                        response.status(200);
                        return "{\"message\":\"Password needs to be at least 8 characters Long.\"}";
                    }));
                }if (user1.getNickName().length() < 6){
                    get(path, "application/json", ((request, response) -> {
                        response.type("application/json");
                        response.status(200);
                        return "{\"message\":\"Set a nickname with more than 6 characters\"}";
                    }));
                }else{
                    get(path, "application/json", ((request, response) -> {
                        response.type("application/json");
                        response.status(201);
                        return "{\"message\":\"User created!\"}";
                    }));
                    em.persist(user1);
                }
            }
            res.type("application/json");
            res.status(200);
            return "{\"message\":\"Username already taken.\"}";
        });
    }
}
