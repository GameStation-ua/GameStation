package webpage.handlers;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import webpage.model.User;
import webpage.requestFormats.RegisterRequest;
import webpage.util.HandlerType;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static spark.Spark.path;
import static spark.Spark.post;
import static webpage.entity.Persister.persist;
import static webpage.entity.Users.findUserByUsername;


public class RegisterHandler extends AbstractHandler {

    public void handle() {
        path("/register", () -> {
            post("", "application/json", (req, res) -> {
                RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
                if (registerRequest.getPassword() == null || registerRequest.getUsername() == null || registerRequest.getNickname() == null) {
                    res.status(406);
                    return "{\"message\":\"You need to fill all the fields\"}";
                }

                Optional<User> user = findUserByUsername(registerRequest.getUsername());
                if (user.isPresent()) {
                    res.status(200);
                    return "{\"message\":\"Username already taken.\"}";
                }
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
                        persist(Optional.of(user1));
                        res.status(201);
                        return "{\"message\":\"User created!\"}";
                    }catch (Throwable r) {
                        res.status(500);
                        return "{\"message\":\"Something went wrong, try again.\"}";
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
