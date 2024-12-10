package websocket.messages;

import chess.ChessGame;
import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    private String errorMessage;
    private final Integer gameID;
    private String message;
    private ChessGame game;
    private ChessGame.TeamColor color;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, Integer gameID, ChessGame game) {
        this.serverMessageType = type;
        this.gameID = gameID;
        this.game = game;
        this.color = game.getTeamTurn();
    }

    public ServerMessage(String errorMessage, Integer gameID){
        this.serverMessageType = ServerMessageType.ERROR;
        this.errorMessage = errorMessage;
        this.gameID = gameID;
    }

    public ServerMessage(ServerMessageType type, String message, Integer gameID){
        this.serverMessageType = type;
        this.message = message;
        this.gameID = gameID;
    }

    public ServerMessageType getServerMessageType() {
        return serverMessageType;
    }

    public String getMessageError(){
        return errorMessage;
    }

    public int getGameID(){
        return gameID;
    }

    public ChessGame getGame(){
        return game;
    }

    public ChessGame.TeamColor getColor(){
        return color;
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
