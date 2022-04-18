package webpage.handlers;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import webpage.entity.Tag;
import webpage.util.HandlerType;

import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static spark.Spark.get;
import static webpage.util.SecretKey.key;

public class HomeHandler extends AbstractHandler{
    public HomeHandler(EntityManagerFactory emf) {
        super(emf);
    }


    public void handle(){
        get("/home", (req, res) -> {
            String token = req.headers("token");
            if (verifyJWT(token)){
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
//                Integer userId =  claims.get("id");
                return "";

            }else{
                res.status(401);
                return "{\"message\":\"Not logged in.\"}";
            }
        });
        get("/isAdmin", (req, res) -> {
            String token = req.headers("token");
            if (verifyJWT(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token).getBody();
                Boolean isAdmin = (Boolean) claims.get("isAdmin");
                return "{\"message\":\"Is admin\"}";
            } else return "{\"message\":\"Not logged in\"}";
        });
    }

    @Override
    public HandlerType getType() {
        return HandlerType.HOME;
    }

    List<Tag> get5Elements(Set<Tag> mySet) {
        List<Tag> asList = new ArrayList<>(mySet);
        Collections.shuffle(asList);
        List<Tag> newList = new ArrayList<>();
        for (Tag tag : asList) {
            newList.add(tag);
            if (newList.size() == 5) return newList;
        }
        return newList;
    }
}

