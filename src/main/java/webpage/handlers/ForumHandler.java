package webpage.handlers;

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
import static webpage.util.Parser.fromJson;
import static webpage.util.Parser.toJson;

public class ForumHandler extends AbstractHandler{

    @Override
    public void handle() {
        path("/forum", () -> {
            get("/thread/:threadId","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");

                Optional<Thread> thread = findThreadById(Long.valueOf(req.params(":threadId")));
                if (thread.isEmpty()) return returnJson(res, 400, "Invalid Thread");

                Optional<User> user = findUserById(thread.get().getCreatorId());
                if (user.isEmpty()) return returnJson(res, 500, "Something went wrong");

                res.status(200);
                return toJson(new ThreadResponse(thread.get(), user.get(), getIdByToken(token)));

            });

            post("/thread/create","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                Long userId = getIdByToken(token);
                ThreadRequest threadRequest = fromJson(req.body(), ThreadRequest.class);
                    
                    try {
                    Thread thread = new Thread(userId, threadRequest.getGameId(), threadRequest.getDescription(), threadRequest.getTitle());
                        persist(thread);
                        return returnJson(res, 200, "OK");
                    }catch (Throwable e){
                        return returnJson(res, 500, "Something went wrong");
                    }
            });

            get("/threadPage/*/*","application/json", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) return returnJson(res, 401, "Not logged in");
                String[] request = req.splat();
                Long gameId = Long.valueOf(request[0]);
                int pageNumber = Integer.parseInt(request[1]);


                Optional<List<Thread>> threads = findThreadsByForumPage(gameId, pageNumber);

                if (threads.isEmpty()) return returnJson(res, 500, "Something went wrong");

                Optional<List<ThreadResponse>> softThreadsResponse = prepareSoftThreadResponse(threads.get(), getIdByToken(token));
                if (softThreadsResponse.isEmpty()) return returnJson(res, 500, "Something went wrong");

                res.status(200);
                return toJson(softThreadsResponse.get());
            });
        });
    }



    @Override
    public HandlerType getType() {
        return HandlerType.FORUM;
    }
}