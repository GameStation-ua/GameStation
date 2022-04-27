package webpage.handlers;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.net.InetSocketAddress;

@WebSocket
public class NotificationHandler {

    @OnWebSocketConnect
    public void onConnect(Session user){
        InetSocketAddress wasd =  user.getLocalAddress();
        UpgradeRequest upgradeRequest = user.getUpgradeRequest();
        RemoteEndpoint remote = user.getRemote();
        String g = "hello";
    }
}
