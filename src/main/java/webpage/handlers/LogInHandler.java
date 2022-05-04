package webpage.handlers;

import webpage.model.User;
import webpage.requestFormats.LogInRequest;
import webpage.util.HandlerType;

import java.util.Optional;

import static spark.Spark.*;
import static webpage.entity.Users.findUserByUsername;
import static webpage.util.Parser.fromJson;

public class LogInHandler extends AbstractHandler{


    public void handle() {
        path("/login", () -> {
            get("", (req, res) -> {
                String token = req.headers("token");
                if (!verifyJWT(token)) {
                    return returnMessage(res, 401, "Not logged in");
                }
                    return returnMessage(res, 200, "Send to home");
            });

            post("", "application/json", (req, res) -> {
                LogInRequest logInRequest = fromJson(req.body(), LogInRequest.class);
                if (!(logInRequest.getUsername() != null && logInRequest.getPassword() != null)) {
                    return returnMessage(res, 401,"You need to fill all the fields");
                }
                Optional<User> user = findUserByUsername(logInRequest.getUsername());
                if (user.isEmpty()){
                    return returnMessage(res, 406,"Incorrect username or password");
                }
                if (user.get().getPassword().equals(logInRequest.getPassword())) {
                    return returnMessage(res,200, generateToken(user.get()));
                } else {
                    return returnMessage(res, 406,"Incorrect username or password");
                }
            });
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.LOG_IN;
    }
}


