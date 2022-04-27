package webpage.handlers;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.net.InetSocketAddress;

@WebSocket
public class NotificationHandler {

    @OnWebSocketConnect
    public void onConnect(Session user){
        UpgradeRequest req = user.getUpgradeRequest();
        String token = req.getHeader("Sec-WebSocket-Protocol");
        UpgradeResponse upgradeResponse = user.getUpgradeResponse();
        upgradeResponse.setAcceptedSubProtocol("ws.localhost:8443/notifications");
    }
}
