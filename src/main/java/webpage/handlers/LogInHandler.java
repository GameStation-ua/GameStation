package webpage.handlers;

import com.google.gson.Gson;
import spark.Response;
import webpage.entity.User;
import webpage.requestFormats.LogInRequest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import static spark.Spark.get;
import static spark.Spark.post;

public class LogInHandler extends AbstractHandler{

    public LogInHandler(EntityManagerFactory emf) {
        super(emf);
    }


    public void handle(String path) {
        enableCORS();
        get(path, (req, res) -> {
            String token = req.headers("token");
            if (verifyJWT(token)){
                res.status(200);
                return "{\"message\":\"Send to home.\"}";
            }else {
                res.status(406);
                return "{\"message\":\"Invalid token.\"}";
            }
        });
        post(path, "application/json", (request, response) -> {
            LogInRequest logInRequest = new Gson().fromJson(request.body(), LogInRequest.class);
            String token = request.headers("token");
            if (token == null) {
                return regularLogIn(response,logInRequest);
            }else {
                if (verifyJWT(token)) {
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
                    response.status(200);
                    return "{\"message\":" + jws + "}";
                } else {
                    response.status(406);
                    return "{\"message\":\"Incorrect username or password\"}";
                }
            } catch (NoResultException e) {
                response.status(406);
                return "{\"message\":\"Incorrect username or password\"}";
            }

        } else {
            response.status(406);
            return "{\"message\":\"You need to fill all the fields\"}";
        }
    }

}


