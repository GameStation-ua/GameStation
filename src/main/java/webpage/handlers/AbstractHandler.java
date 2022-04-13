package webpage.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import webpage.entity.User;

import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static spark.Spark.before;
import static spark.Spark.options;
import static webpage.util.SecretKey.key;

public abstract class AbstractHandler{
        EntityManagerFactory emf;

        public AbstractHandler(EntityManagerFactory emf) {
                this.emf = emf;
        }
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
                        // Note: this may or may not be necessary in your particular application
                        response.type("application/json");
                });
        }

        boolean verifyJWT(String token){
            String[] chunks = token.split("\\.");
            if (chunks.length != 3) return false;
            Base64.Decoder decoder = Base64.getUrlDecoder();
//            String header = new String(decoder.decode(chunks[0]));
//            String payload = new String(decoder.decode(chunks[1]));
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
                .signWith(HS256, TextCodec.BASE64.decode(key)
                ).compact();
    }
}
