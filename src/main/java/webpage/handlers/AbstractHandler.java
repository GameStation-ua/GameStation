package webpage.handlers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import spark.Response;
import webpage.model.User;
import webpage.util.Handler;

import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static webpage.util.SecretKey.key;

public abstract class AbstractHandler implements Handler {


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
                .signWith(HS256, TextCodec.BASE64.decode(key))
                .compact();
    }

    public static String returnJson(Response res, Integer status, String message){
            res.status(status);
            res.header("Content-Type", "application/json");
            return "{\"message\":\"" + message + "\"}";
    }

}
