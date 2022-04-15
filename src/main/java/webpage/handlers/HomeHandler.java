package webpage.handlers;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.persistence.EntityManagerFactory;

import static spark.Spark.get;
import static webpage.util.SecretKey.key;

public class HomeHandler extends AbstractHandler{
    public HomeHandler(EntityManagerFactory emf) {
        super(emf);
    }


    public void handle(String path){
//        get(path, (req, res) -> {
//            String token = req.headers("token");
//            if (verifyJWT(token)){
//                Claims claims = Jwts.parser()
//                        .setSigningKey(key)
//                        .parseClaimsJws(token).getBody();
//                String userId =  (String) claims.get("id");
//                Integer userId1 = Integer.parseInt(userId);
//                boolean isAdmin = (boolean) claims.get("isAdmin");
//
//            }else{
//                res.status(401);
//                return "{\"message\":\"Not logged in.\"}";
//            }
//        });
    }
}
