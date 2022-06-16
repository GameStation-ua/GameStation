package webpage.handlers;

import com.google.common.hash.Hashing;
import webpage.model.User;
import webpage.requestFormats.RegisterRequest;
import webpage.util.HandlerType;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static spark.Spark.path;
import static spark.Spark.post;
import static webpage.entity.Persister.persist;
import static webpage.entity.Users.findUserByUsername;
import static webpage.util.Parser.fromJson;


public class RegisterHandler extends AbstractHandler {

    public void handle() {
        path("/register", () -> {
            post("", "application/json", (req, res) -> {
                RegisterRequest registerRequest = fromJson(req.body(), RegisterRequest.class);
                if (registerRequest.getPassword() == null || registerRequest.getUsername() == null || registerRequest.getNickname() == null) return returnMessage(res, 406, "You need to fill all the fields");

                Optional<User> user = findUserByUsername(registerRequest.getUsername());
                if (user.isPresent()) return returnMessage(res, 200, "Username already taken");
                if (!(checkString(registerRequest.getPassword())) || (registerRequest.getPassword().length() < 8)) {
                    return returnMessage(res, 200, "You need to meet password requirements");
                } else {
                    registerRequest.setPassword(Hashing.sha512().hashString(registerRequest.getPassword(), StandardCharsets.UTF_8).toString());
                    User user1 = new User(registerRequest.getNickname(),
                            registerRequest.getUsername(),
                            registerRequest.getPassword());
                    try{
                        persist(user1);
                        return returnMessage(res, 201, "User created!");
                    }catch (Throwable r) {
                        return returnMessage(res, 500, "Something went wrong, try again");
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
