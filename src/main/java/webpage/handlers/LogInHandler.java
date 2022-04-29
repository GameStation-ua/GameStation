package webpage.handlers;

import com.google.gson.Gson;
import webpage.entity.User;
import webpage.requestFormats.LogInRequest;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import static spark.Spark.*;
import static webpage.util.EntityManagers.close;
import static webpage.util.EntityManagers.currentEntityManager;

public class LogInHandler extends AbstractHandler{


    public void handle() {
        path("login", () -> {
            get("", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    res.status(200);
                    return "{\"message\":\"Send to home.\"}";
                } else {
                    res.status(406);
                    return "{\"message\":\"Invalid token.\"}";
                }
            });

            post("", "application/json", (request, response) -> {
                LogInRequest logInRequest = new Gson().fromJson(request.body(), LogInRequest.class);
                if (logInRequest.getUsername() != null && logInRequest.getPassword() != null) {
                    
                    try {
                        User user = (User) currentEntityManager().createQuery("FROM User u WHERE u.username = :username")
                                .setParameter("username", logInRequest.getUsername())
                                .getSingleResult();
                        if (user.getPassword().equals(logInRequest.getPassword())) {
                            String jws = generateToken(user);
                            response.status(200);
                            return jws;
                        } else {
                            response.status(406);
                            return "{\"message\":\"Incorrect username or password\"}";
                        }
                    } catch (NoResultException e) {
                        response.status(406);
                        return "{\"message\":\"Incorrect username or password\"}";
                    }finally {
                        close();
                    }
                } else {
                    response.status(406);
                    return "{\"message\":\"You need to fill all the fields\"}";
                }
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.LOG_IN;
    }
}


