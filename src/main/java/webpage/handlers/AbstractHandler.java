package webpage.handlers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import webpage.model.User;
import webpage.util.Handler;

import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static spark.Spark.before;
import static spark.Spark.options;
import static webpage.util.SecretKey.key;

public abstract class AbstractHandler implements Handler {


        public void enableCORS() {
                options("/*", (request, response) -> {

                        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                        if (accessControlRequestHeaders != null) {
                                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
                        }

                        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                        if (accessControlRequestMethod != null) {
                                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                        }

                        return "OK";
                });

                before((request, response) -> {
                        response.header("Access-Control-Allow-Origin", "*");
                        response.header("Access-Control-Request-Method", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
                        response.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
                        response.type("application/json");
                });
        }

        boolean verifyJWT(String token){
            if (token == null) return false;
            String[] chunks = token.split("\\.");
            if (chunks.length != 3) return false;
            SignatureAlgorithm sa = HS256;
            SecretKeySpec secretKeySpec = new SecretKeySpec(TextCodec.BASE64.decode(key), sa.getJcaName());
            String tokenWithoutSignature = chunks[0] + "." + chunks[1];
            String signature = chunks[2];
            DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
            return validator.isValid(tokenWithoutSignature, signature);
        }

    protected String generateToken(User user) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 90);
        dt = c.getTime();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(dt)
                .setIssuedAt(new Date())
                .claim("id", user.getId())
                .claim("isAdmin", user.isAdmin())
                .signWith(HS256, TextCodec.BASE64.decode(key)
                ).compact();
    }


}
