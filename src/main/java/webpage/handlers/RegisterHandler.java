package webpage.handlers;

import com.google.common.hash.Hashing;
import webpage.model.User;
import webpage.requestFormats.RegisterRequest;
import webpage.util.HandlerType;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static spark.Spark.path;
import static spark.Spark.post;
import static webpage.entity.Persister.persist;
import static webpage.entity.Users.findEmailIfInUse;
import static webpage.entity.Users.findUserByUsername;
import static webpage.util.Parser.fromJson;


public class RegisterHandler extends AbstractHandler {

    public void handle() {
        path("/register", () -> {
            post("", "application/json", (req, res) -> {
                RegisterRequest registerRequest = fromJson(req.body(), RegisterRequest.class);
                if (registerRequest.getPassword() == null || registerRequest.getUsername() == null || registerRequest.getNickname() == null) return returnJson(res, 406, "You need to fill all the fields");

                Optional<User> user = findUserByUsername(registerRequest.getUsername());
                if (user.isPresent()) return returnJson(res, 200, "Username already taken");
                if (findEmailIfInUse(registerRequest.getEmail()).isPresent()) return returnJson(res, 200, "Email already in use");
                if (!(checkString(registerRequest.getPassword())) || (registerRequest.getPassword().length() < 8)) {
                    return returnJson(res, 200, "You need to meet password requirements");
                } else {
                    registerRequest.setPassword(Hashing.sha512().hashString(registerRequest.getPassword(), StandardCharsets.UTF_8).toString());
                    User user1 = new User(registerRequest.getNickname(),
                            registerRequest.getUsername(),
                            registerRequest.getPassword(),
                            registerRequest.getEmail());
                    try{
                        persist(user1);
                        return returnJson(res, 201, "User created!");
                    }catch (Throwable r) {
                        return returnJson(res, 500, "Something went wrong, try again");
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

    private boolean testMail(String emailAddress) {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");

        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }
}
