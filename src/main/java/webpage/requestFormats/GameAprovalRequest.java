package webpage.requestFormats;

public class GameAprovalRequest {

    private final boolean isApproved;
    private final Long gameRequestId;


    public GameAprovalRequest(boolean isApproved, Long gameRequestId) {
        this.isApproved = isApproved;
        this.gameRequestId = gameRequestId;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public Long getGameRequestId() {
        return gameRequestId;
    }
}
