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
    public void onConnect(Session user) throws Exception{
        UpgradeRequest req = user.getUpgradeRequest();
        String token = req.getHeader("Sec-WebSocket-Protocol");
        user.getUpgradeResponse().setAcceptedSubProtocol(token);
    }
}
