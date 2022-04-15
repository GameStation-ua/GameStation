package webpage.handlers;

import com.google.gson.Gson;
import webpage.entity.Tag;
import webpage.entity.User;
import webpage.requestFormats.UserTagsRequest;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;

import static spark.Spark.get;

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
    public void handle(String path) {
        get(path, (req,res) -> {
//        User user = new User();
//        user.setPassword("12345678");
//        user.setNickname("pedro");
//        user.setId(456789789);
//        User user2 = new User();
//        user2.setPassword("12345678");
//        user2.setNickname("pedro");
//        user2.setAdmin(true);
//        user2.setId(456789789);
//        ArrayList<User> users = new ArrayList<>();
//        users.add(user);
//        users.add(user2);
//        POJO pojo = new POJO("cosas", users);
            UserTagsRequest tagsRequest = new UserTagsRequest();
            Tag tag1 = new Tag(); tag1.setName("wasd");
            Tag tag2 = new Tag(); tag2.setName("ert");
            Tag tag3 = new Tag(); tag3.setName("qwerty");
            ArrayList<Tag> tags = new ArrayList<>(); tags.add(tag1);tags.add(tag2);tags.add(tag3);
            tagsRequest.setTags(tags);
            tagsRequest.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoiMTUxNjIzOTAyMiIsImlkIjoiMSJ9.OWIP66rhu8RQrqi0S5ISjwUusDXFP2P2fI0xfuI5EO4");
        Gson gson = new Gson();
            return gson.toJson(tagsRequest);
        });
    }
}
class POJO{
    String data;
    ArrayList<User> users;

    public POJO(String data, ArrayList<User> users) {
        this.data = data;
        this.users = users;
    }
}



