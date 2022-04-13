package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import webpage.entity.User;
import webpage.requestFormats.LogInRequest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static spark.Spark.post;
import static webpage.util.SecretKey.key;

public class LogInHandler extends AbstractHandler{

    public LogInHandler(EntityManagerFactory emf) {
        super(emf);
    }


    public void handle(String path) throws IOException {
        post(path, "application/json", (request, response) -> {
            LogInRequest logInRequest = new Gson().fromJson(request.body(), LogInRequest.class);
            verifyJWT(logInRequest.getToken());
           if (logInRequest.getToken() != null && isValidToken(logInRequest.getToken())) {
               response.status(200);
               return "{\"message\":\"Send to login.\"}";
           }else{
               if (logInRequest.getUsername() != null && logInRequest.getPassword() != null) {
                   EntityManager em = emf.createEntityManager();
                   Query query1 = em.createQuery("FROM User u WHERE u.username = :username");
                   query1.setParameter("username", logInRequest.getUsername());
                   try{
                       User user = (User) query1.getSingleResult();
                       if (user.getPassword().equals(logInRequest.getPassword())) {
                           Date dt = new Date();
                           Calendar c = Calendar.getInstance();
                           c.setTime(dt);
                           c.add(Calendar.DATE, 90);
                           dt = c.getTime();
                           String jws = Jwts.builder()
                                   .setSubject(user.getUsername())
                                   .setExpiration(dt)
                                   .setIssuedAt(new Date())
                                   .claim("id", user.getId())
                                   .signWith(SignatureAlgorithm.HS256,TextCodec.BASE64.decode(key)
                                   ).compact();
                           user.setToken(jws);
                           em.getTransaction().begin();
                           em.merge(user);
                           em.getTransaction().commit();
                           em.close();
                           return "{\"message\":" + jws + "}";
                       }else{
                           return "{\"message\":\"Incorrect username or password\"}";
                       }
                   }catch (NoResultException e){
                       return "{\"message\":\"Incorrect username or password\"}";
                   }

               }
               else {
                   response.status(201);
                   return "{\"message\":\"You need to fill all the fields\"}";
               }
           }

        });

    }

    private boolean isValidToken(String token) {
        return false;
    }
}


