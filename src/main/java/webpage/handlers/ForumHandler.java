package webpage.handlers;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Thread;
import webpage.entity.User;
import webpage.requestFormats.ThreadRequest;
import webpage.responseFormats.ThreadResponse;
import webpage.util.HandlerType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;
import static webpage.util.EntityManagers.close;
import static webpage.util.EntityManagers.currentEntityManager;
import static webpage.util.SecretKey.key;

public class ForumHandler extends AbstractHandler{

    @Override
    public void handle() {
        path("/forum", () -> {
            get("/thread/:threadId", (req, res) -> {
                    String token = req.headers("token");
                    if (verifyJWT(token)) {
                        
                        try {
                            Thread thread = (Thread) currentEntityManager().createQuery("FROM Thread t WHERE t.id = ?1")
                                    .setParameter(1, Long.valueOf(req.params(":threadId")))
                                    .getSingleResult();
                            User user = (User) currentEntityManager().createQuery("FROM User u WHERE u.id = ?1")
                                    .setParameter(1, thread.getCreatorId())
                                    .getSingleResult();
                            return new Gson().toJson(new ThreadResponse(thread, user));
                        }catch (NoResultException e){
                            res.status(400);
                            return "{\"message\":\"Invalid Thread.\"}";
                        }finally {
                            close();
                        }
                    }else {
                        res.status(401);
                        return "{\"message\":\"Not logged in.\"}";
                    }
            });

            post("/thread/create", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)) {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token).getBody();
                    Long userId = Long.valueOf((Integer) claims.get("id"));
                    Gson gson = new Gson();
                    ThreadRequest threadRequest = gson.fromJson(req.body(), ThreadRequest.class);
                    
                    try {
                    Thread thread = new Thread(userId, threadRequest.getGameId(), threadRequest.getDescription(), threadRequest.getTitle());
                        currentEntityManager().getTransaction().begin();
                        currentEntityManager().persist(thread);
                        currentEntityManager().getTransaction().commit();
                        res.status(200);
                        return  "{\"message\":\"OK.\"}";
                    }catch (Throwable e){
                        currentEntityManager().getTransaction().rollback();
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }finally {
                        close();
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });

            get("/*/*", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)){
                    String[] request = req.splat();
                    Long gameId = Long.valueOf(request[0]);
                    int pageNumber = Integer.parseInt(request[1]);
                    
                    try{
                        @SuppressWarnings("unchecked") List<Thread> threads = currentEntityManager().createQuery("FROM Thread t WHERE t.gameId = ?1")
                                .setParameter(1, gameId)
                                .setFirstResult(pageNumber * 10 - 10)
                                .setMaxResults(10)
                                .getResultList();

                        List<ThreadResponse> softThreadsResponse = new ArrayList<>();
                        for (Thread thread : threads) {
                            User user = (User) currentEntityManager().createQuery("FROM User u WHERE u.id = ?1")
                                            .setParameter(1, thread.getCreatorId())
                                            .getSingleResult();
                            softThreadsResponse.add(new ThreadResponse(thread, user));
                        }
                        return new Gson().toJson(softThreadsResponse);
                    }catch (Throwable e){
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }finally {
                        close();
                    }
                }else {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
            });


        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.FORUM;
    }
}