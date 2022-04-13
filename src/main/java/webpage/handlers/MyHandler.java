package webpage.handlers;

import com.google.gson.Gson;
import webpage.entity.User;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.post;

public class MyHandler extends AbstractHandler{
    public MyHandler(EntityManagerFactory emf) {
        super(emf);
    }

    //    public void handle(EntityManagerFactory emf, String path) throws IOException {
//        post("/main", "image/png", (req, res) -> {
//            byte[] bytes = req.bodyAsBytes();
//            BufferedImage image = new BufferedImage(75,75,BufferedImage.TYPE_INT_RGB);
//            long id = req.attribute("Id");
//            File outputfile = new File( id + ".png");
////            ImageIO.write(bufferedImage, "png", outputfile);
//            return "{\"message\":\"Password needs to be at least 8 characters Long.\"}";
//        });
//    }
//}
    public void handle(String path) throws IOException {
        User user = new User();
        user.setPassword("12345678");
        user.setNickname("pedro");
        user.setId(456789789);
        User user2 = new User();
        user2.setPassword("12345678");
        user2.setNickname("pedro");
        user2.setId(456789789);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        Gson gson = new Gson();
        String s = gson.toJson(users);
        post(path, (req,res) -> {
            return s;
        });
    }
}



