package webpage.handlers;

import com.google.gson.Gson;
import spark.Response;
import webpage.entity.User;
import webpage.requestFormats.LogInRequest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.IOException;

import static spark.Spark.post;

public class LogInHandler extends AbstractHandler{

    public LogInHandler(EntityManagerFactory emf) {
        super(emf);
    }


    public void handle(String path) {
        post(path, "application/json", (request, response) -> {
            LogInRequest logInRequest = new Gson().fromJson(request.body(), LogInRequest.class);
            if (logInRequest.getToken() == null) {
                return regularLogIn(response,logInRequest);
            }else {
                if (verifyJWT(logInRequest.getToken())) {
                    response.status(200);
                    return "{\"message\":\"Send to home.\"}";
                } else {
                    return regularLogIn(response, logInRequest);
                }
            }
        });

    }

    private String regularLogIn(Response response, LogInRequest logInRequest) {
        if (logInRequest.getUsername() != null && logInRequest.getPassword() != null) {
            EntityManager em = emf.createEntityManager();
            Query query1 = em.createQuery("FROM User u WHERE u.username = :username");
            query1.setParameter("username", logInRequest.getUsername());
            try {
                User user = (User) query1.getSingleResult();
                if (user.getPassword().equals(logInRequest.getPassword())) {
                    String jws = generateToken(user);
                    return "{\"message\":" + jws + "}";
                } else {
                    return "{\"message\":\"Incorrect username or password\"}";
                }
            } catch (NoResultException e) {
                return "{\"message\":\"Incorrect username or password\"}";
            }

        } else {
            response.status(201);
            return "{\"message\":\"You need to fill all the fields\"}";
        }
    }

}


