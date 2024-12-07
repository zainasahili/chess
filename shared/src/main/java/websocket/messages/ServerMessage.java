package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    private String errorMessage;
    private Integer game;
    private String message;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, Integer game) {
        this.serverMessageType = type;
        this.game = game;
    }

    public ServerMessage(String errorMessage, Integer game){
        this.serverMessageType = ServerMessageType.ERROR;
        this.errorMessage = errorMessage;
        this.game = game;
    }

    public ServerMessage(ServerMessageType type, String message, Integer game){
        this.serverMessageType = type;
        this.message = message;
        this.game = game;
    }

    public ServerMessageType getServerMessageType() {
        return serverMessageType;
    }

    public String getMessageError(){
        return errorMessage;
    }

    public int getGame(){
        return game;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
