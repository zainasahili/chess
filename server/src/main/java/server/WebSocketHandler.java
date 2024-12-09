package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session session){
        Server.sessions.put(session, 0);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Server.sessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, BadRequestException, IOException {
        UserGameCommand msg = new Gson().fromJson(message, UserGameCommand.class);

        if (msg.getCommandType().equals(UserGameCommand.CommandType.MAKE_MOVE)){
            makeMove(session, msg);
        }
        else if (msg.getCommandType().equals(UserGameCommand.CommandType.LEAVE)){
            leave(session, msg);
        }
        else if (msg.getCommandType().equals(UserGameCommand.CommandType.RESIGN)){
            resign(session, msg);
    }
         else if (msg.getCommandType().equals(UserGameCommand.CommandType.CONNECT)){
            connect(session, msg);
        }
    }

    private void connect(Session session, UserGameCommand msg) throws IOException{
        AuthData authData;
        GameData gameData;

        try {
            // Attempt to get auth data
            authData = Server.authDAO.getAuth(msg.getAuthToken());
            if (authData == null) {
                throw new BadRequestException("Invalid AuthToken");
            }

            // Attempt to get game data
            gameData = Server.gameDAO.getGame(msg.getGameID());
            if (gameData == null) {
                throw new BadRequestException("Invalid Game ID");
            }
        } catch (DataAccessException | BadRequestException e) {
            // Send an error message to the client
            ServerMessage errorMessage = new ServerMessage(e.getMessage(), null);
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        ServerMessage command = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, msg.getGameID());
        session.getRemote().sendString(new Gson().toJson(command));

        ChessGame.TeamColor color = getColor(gameData, authData.username());
        if (color != null) {
            boolean player;
            if (color == ChessGame.TeamColor.WHITE) {
                player = Objects.equals(authData.username(), gameData.whiteUsername());
            } else {
                player = Objects.equals(authData.username(), gameData.blackUsername());
            }
            if (!player){
                ServerMessage errorMessage = new ServerMessage("you are trying to join with the wrong color", gameData.gameID());
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                return;
            }
            String note = String.format("%s has joined the game as %s", authData.username(), color);
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, note, null);
            announceNotification(session, notify, "notUser", gameData.gameID());

        } else{
            String note =  "%s has joined the game as an observer".formatted(authData.username());
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,note, null);
            announceNotification(session, notify, "notUser", gameData.gameID());
        }
        Server.sessions.put(session, gameData.gameID());

    }
    private void makeMove(Session session, UserGameCommand msg) throws DataAccessException, BadRequestException, IOException {
        AuthData authData;
        try {
            // Attempt to get auth data
            authData = Server.authDAO.getAuth(msg.getAuthToken());
            if (authData == null) {
                throw new BadRequestException("Invalid AuthToken");
            }
        } catch (DataAccessException e){
            ServerMessage errorMessage = new ServerMessage("Invalid AuthToken", null);
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        GameData gameData = Server.gameDAO.getGame(msg.getGameID());

        ChessGame.TeamColor color =  getColor(gameData, authData.username());

        if (gameData.game().getTeamTurn() != color){
            ServerMessage errorMessage = new ServerMessage("It's not your turn", null);
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        } else if (color == null){
            ServerMessage error = new ServerMessage("You are an observer. you can't make a move", null);
            session.getRemote().sendString(new Gson().toJson(error));
        } else if (gameData.game().isGameOver()){
            ServerMessage error = new ServerMessage("Game is already over", null);
            session.getRemote().sendString(new Gson().toJson(error));
        } else if (gameData.game().getTeamTurn().equals(color)){
            try{
                gameData.game().makeMove(msg.getMove());
            } catch (InvalidMoveException e) {
                ServerMessage error = new ServerMessage("Invalid move", null);
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            Server.gameDAO.updateGame(gameData);

            ServerMessage notification;
            ChessGame.TeamColor oppColor = color == ChessGame.TeamColor.WHITE? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

            ServerMessage command = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, msg.getGameID());
            announceNotification(session,command, "everyone", gameData.gameID());
            String note =  "a move has been made by %s".formatted(authData.username());
            notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,note, null);
            announceNotification(session, notification, "notUser", gameData.gameID());

            if (gameData.game().isInCheckmate(oppColor)){
                note =  "Checkmate! %s wins".formatted(authData.username());
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,note, null);
                announceNotification(session, notification, "everyone", gameData.gameID());
                gameData.game().setGameOver(true);
            } else if (gameData.game().isInCheck(oppColor)){
                note = "Because of the last move, %s is now in check".formatted(oppColor.toString());
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, note, gameData.gameID());
                announceNotification(session, notification, "everyone", gameData.gameID());
            } else if (gameData.game().isInStalemate(oppColor)){
                note = "The game is in Stalemate. It's a tie!";
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, note, gameData.gameID());
                gameData.game().setGameOver(true);
                announceNotification(session, notification, "everyone", gameData.gameID());
            }

        }
    }
    private void leave(Session session, UserGameCommand msg) throws IOException {
        try{
            AuthData auth = Server.authDAO.getAuth(msg.getAuthToken());
            String note =  "%s has left the game".formatted(auth.username());
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,note , null);
            announceNotification(session, notify, "notUser", msg.getGameID());
            Server.sessions.remove(session);
            GameData gameData = Server.gameDAO.getGame(msg.getGameID());

            if (auth.username().equals(gameData.whiteUsername())){
                gameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else if (auth.username().equals(gameData.blackUsername())){
                gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            }

            Server.gameDAO.updateGame(gameData);
            session.close();

        } catch (DataAccessException | IOException e) {
            ServerMessage error = new ServerMessage("Not authorized", null);
            session.getRemote().sendString(new Gson().toJson(error));
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    private void resign(Session session, UserGameCommand msg){
        try{
            AuthData auth = Server.authDAO.getAuth(msg.getAuthToken());
            GameData gameData = Server.gameDAO.getGame(msg.getGameID());
            ChessGame.TeamColor color = getColor(gameData, auth.username());
            if (color == null){
                ServerMessage error = new ServerMessage("You are an observer. you can't resign", null);
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            String oppUsername = color == ChessGame.TeamColor.WHITE? gameData.whiteUsername() : gameData.blackUsername();

            if (gameData.game().isGameOver()){
                ServerMessage error = new ServerMessage("Game is already over", null);
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            gameData.game().setGameOver(true);
            Server.gameDAO.updateGame(gameData);
            String note = "%s has resigned. %s wins!!".formatted(auth.username(), oppUsername);
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,note, null);
            session.getRemote().sendString(new Gson().toJson(notify));
            announceNotification(session, notify, "notUser", gameData.gameID());
            Server.sessions.remove(session);
            session.close();
        } catch (DataAccessException | BadRequestException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void announceNotification(Session currSession, ServerMessage notification, String audience, int gameID) throws IOException {
        // to everyone, user, notUser in current game
        for(var session :Server.sessions.entrySet()){
            boolean in = session.getValue() == gameID;
            boolean  self = session.getKey().equals(currSession);
            switch (audience){
                case("everyone"):
                    if (in) {
                        session.getKey().getRemote().sendString(new Gson().toJson(notification));
                    }
                    break;
                case "notUser":
                    if (!self && in){
                        session.getKey().getRemote().sendString(new Gson().toJson(notification));
                    }
                    break;
            }
        }
    }
    private ChessGame.TeamColor getColor(GameData gameData, String username){
        if (username.equals(gameData.whiteUsername())){
            return ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUsername())){
            return ChessGame.TeamColor.BLACK;
        } else{
            return null;
        }
    }

}
