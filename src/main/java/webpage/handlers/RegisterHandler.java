package webpage.handlers;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import webpage.entity.User;
import webpage.requestFormats.RegisterRequest;
import webpage.util.HandlerType;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;

import static spark.Spark.path;
import static spark.Spark.post;


public class RegisterHandler extends AbstractHandler {

    public RegisterHandler(EntityManagerFactory emf) {
        super(emf);
    }

    public void handle() {
        path("/register", () -> {
            post("", "application/json", (req, res) -> {
                RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
                if (registerRequest.getPassword() == null || registerRequest.getUsername() == null || registerRequest.getNickname() == null) {
                    res.status(406);
                    return "{\"message\":\"You need to fill all the fields\"}";
                }
                final EntityManager em = emf.createEntityManager();

                try {
                    em.createQuery("SELECT user FROM User user WHERE user.username like :username")
                            .setParameter("username", registerRequest.getUsername())
                            .getSingleResult();
                    res.type("application/json");
                    res.status(200);
                    return "{\"message\":\"Username already taken.\"}";
                } catch (NoResultException e) {
                    if (!(checkString(registerRequest.getPassword())) || (registerRequest.getPassword().length() < 8)) {
                        res.type("application/json");
                        res.status(200);
                        return "{\"message\":\"You need to meet password requirements.\"}";
                    } else {
                        res.type("application/json");
                        registerRequest.setPassword(Hashing.sha512().hashString(registerRequest.getPassword(), StandardCharsets.UTF_8).toString());
                        User user1 = new User(registerRequest.getNickname(),
                                registerRequest.getUsername(),
                                registerRequest.getPassword());
                        try{
                        em.getTransaction().begin();
                        em.persist(user1);
                        em.getTransaction().commit();
                        res.status(201);
                        return "{\"message\":\"User created!\"}";
                        }catch (Throwable r) {
                            em.getTransaction().rollback();
                            res.status(500);
                            return "{\"message\":\"Something went wrong, try again.\"}";
                        }
                    }
                }
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.REGISTER;
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
