package cin.ufpe.br.sdbuttomapp.network;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import cin.ufpe.br.sdbuttomapp.service.NotificationService;

public class WSClient extends WebSocketClient {

    NotificationService notification;

    public WSClient(URI serverUri) {
        super(serverUri);
    }

    public void setNotification(NotificationService notification) {
        this.notification = notification;
    }

    @Override
    public void onOpen( ServerHandshake handshakedata ) {
        System.out.println( "opened connection" );
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(final String message ) {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (notification != null) {
                    notification.on(message);
                }
            }
        }).start();
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
    }

    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}
