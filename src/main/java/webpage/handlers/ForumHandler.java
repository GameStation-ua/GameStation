package webpage.handlers;


import webpage.util.HandlerType;

import javax.persistence.EntityManagerFactory;

import static spark.Spark.get;
import static spark.Spark.path;

public class ForumHandler extends AbstractHandler{
    public ForumHandler(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public void handle() {
        path("/forum", () -> {
            get("/:gameId", (req, res) -> {
                String token = req.headers("token");
                if (verifyJWT(token)){
                    return "";
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
