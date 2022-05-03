package webpage.handlers;

import com.google.gson.Gson;
import webpage.model.Thread;
import webpage.model.User;
import webpage.requestFormats.ThreadRequest;
import webpage.responseFormats.ThreadResponse;
import webpage.util.HandlerType;

import java.util.List;
import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Persister.persist;
import static webpage.entity.Threads.*;
import static webpage.entity.Users.findUserById;
import static webpage.entity.Users.getIdByToken;

public class ForumHandler extends AbstractHandler{

    @Override
    public void handle() {
        path("/forum", () -> {
            get("/thread/:threadId", (req, res) -> {
                    String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }

                Optional<Thread> thread = findThreadById(Long.valueOf(req.params(":threadId")));
                if (thread.isEmpty()){
                    res.status(400);
                    return "{\"message\":\"Invalid Thread.\"}";
                }

                Optional<User> user = findUserById(thread.get().getCreatorId());
                return new Gson().toJson(new ThreadResponse(thread.get(), user.get()));

            });

            post("/thread/create", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                    Long userId = getIdByToken(token);
                    Gson gson = new Gson();
                    ThreadRequest threadRequest = gson.fromJson(req.body(), ThreadRequest.class);
                    
                    try {
                    Thread thread = new Thread(userId, threadRequest.getGameId(), threadRequest.getDescription(), threadRequest.getTitle());
                        persist(thread);
                        res.status(200);
                        return  "{\"message\":\"OK.\"}";
                    }catch (Throwable e){
                        res.status(500);
                        return "{\"message\":\"Something went wrong.\"}";
                    }
            });

            get("/*/*", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                String[] request = req.splat();
                Long gameId = Long.valueOf(request[0]);
                int pageNumber = Integer.parseInt(request[1]);


                Optional<List<Thread>> threads = findThreadsByForumPage(gameId, pageNumber);

                if (threads.isEmpty()){
                    res.status(500);
                    return "{\"message\":\"Something went wrong.\"}";
                }

                List<ThreadResponse> softThreadsResponse = prepareSoftThreadResponse(threads.get());
                return new Gson().toJson(softThreadsResponse);
            });
        });
    }



    @Override
    public HandlerType getType() {
        return HandlerType.FORUM;
    }
}