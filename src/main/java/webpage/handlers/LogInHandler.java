package webpage.handlers;

import com.google.gson.Gson;
import webpage.model.User;
import webpage.requestFormats.LogInRequest;
import webpage.util.HandlerType;

import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Users.findUserByUsername;

public class LogInHandler extends AbstractHandler{


    public void handle() {
        path("/login", () -> {
            get("", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    res.status(401);
                    return "{\"message\":\"Not logged in.\"}";
                }
                    res.status(200);
                    return "{\"message\":\"Send to home.\"}";
            });

            post("", "application/json", (request, response) -> {
                LogInRequest logInRequest = new Gson().fromJson(request.body(), LogInRequest.class);
                if (!(logInRequest.getUsername() != null && logInRequest.getPassword() != null)) {
                    response.status(406);
                    return "{\"message\":\"You need to fill all the fields\"}";
                }
                Optional<User> user = findUserByUsername(logInRequest.getUsername());
                if (user.isEmpty()){
                    response.status(406);
                    return "{\"message\":\"Incorrect username or password\"}";
                }
                if (user.get().getPassword().equals(logInRequest.getPassword())) {
                    String jws = generateToken(user.get());
                    response.status(200);
                    return jws;
                } else {
                    response.status(406);
                    return "{\"message\":\"Incorrect username or password\"}";
                }
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.LOG_IN;
    }
}


