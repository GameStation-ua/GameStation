package webpage.requestFormats;

public class FollowRequest {
    private final String path;

    public FollowRequest(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
