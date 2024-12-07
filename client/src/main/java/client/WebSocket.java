package client;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

public class WebSocket extends Endpoint {

    Session session;

    public WebSocket(String ServerDomain) throws Exception {
        try {
            URI uri = new URI("ws://" + ServerDomain + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler((MessageHandler.Whole<String>) this::handleMessage);
        } catch (URISyntaxException | DeploymentException | IOException e){
            throw new Exception();
        }
    }
    private void handleMessage(String message){
        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
        if (msg.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)){
            System.out.println(msg.getGame());
        } else if (msg.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){
            System.out.println(msg.getMessageError());
        } else if (msg.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)){
            System.out.println(msg.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void sendMessage(String message){
        this.session.getAsyncRemote().sendText(message);
    }
}
