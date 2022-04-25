package webpage.responseFormats;

public class SoftThreadResponse {

    private final String title;
    private final String descripcion;
    private final Long userId;

    public SoftThreadResponse(String title, String descripcion, Long userId) {
        this.title = title;
        this.descripcion = descripcion;
        this.userId = userId;
    }
}
