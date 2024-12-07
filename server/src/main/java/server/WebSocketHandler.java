package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
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


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, BadRequestException, IOException, InvalidMoveException {
        UserGameCommand msg = new Gson().fromJson(message, UserGameCommand.class);

        if (msg.getCommandType().equals(UserGameCommand.CommandType.MAKE_MOVE)){
            makeMove(session, msg);
        }
//        else if (msg.getCommandType().equals(UserGameCommand.CommandType.LEAVE)){
//            leave(session, msg);
//        } else if (msg.getCommandType().equals(UserGameCommand.CommandType.RESIGN)){
//            resign(session, msg);
//    }
         else if (msg.getCommandType().equals(UserGameCommand.CommandType.CONNECT)){
            connect(session, msg);
        }
    }

    private void connect(Session session, UserGameCommand msg) throws IOException, DataAccessException, BadRequestException {
        AuthData authData = Server.authDAO.getAuth(msg.getAuthToken());
        GameData gameData = Server.gameDAO.getGame(msg.getGameID());
        if (gameData == null){
            ServerMessage errorMessage = new ServerMessage("invalid game ID", null);
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        } else if (authData == null){
            ServerMessage errorMessage = new ServerMessage("invalid AuthToken", null);
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
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has joined the game as %s", authData.username(), color), null);
            announceNotification(session, notify, msg.getAuthToken(), "notUser");

        } else{
            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "%s has joined the game as an observer".formatted(authData.username()), null);
            announceNotification(session, notify, msg.getAuthToken(), "notUser");
        }


    }
    private void makeMove(Session session, UserGameCommand msg) throws DataAccessException, BadRequestException, IOException, InvalidMoveException {
        AuthData authData = Server.authDAO.getAuth(msg.getAuthToken());
        GameData gameData = Server.gameDAO.getGame(msg.getGameID());
        ChessGame.TeamColor color =  getColor(gameData, authData.username());

        if (color == null){
            ServerMessage error = new ServerMessage("You are an observer. you can't make a move", gameData.gameID());
            session.getRemote().sendString(new Gson().toJson(error));
        } else if (gameData.game().isGameOver()){
            ServerMessage error = new ServerMessage("Game is already over", gameData.gameID());
            session.getRemote().sendString(new Gson().toJson(error));
        } else if (gameData.game().getTeamTurn().equals(color)){
            gameData.game().makeMove(msg.getMove());
            Server.gameDAO.updateGame(gameData);

            ServerMessage command = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, msg.getGameID());
            session.getRemote().sendString(new Gson().toJson(command));

            ServerMessage notification;
            ChessGame.TeamColor oppColor = color == ChessGame.TeamColor.WHITE? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

            notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, " a move has been made by %s".formatted(authData.username()), gameData.gameID());
            announceNotification(session, notification, msg.getAuthToken(), "notUser");

            if (gameData.game().isInCheckmate(oppColor)){
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Checkmate! %s wins".formatted(authData.username()), gameData.gameID());
                gameData.game().setGameOver(true);
                announceNotification(session, notification, msg.getAuthToken(), "everyone");
            } else if (gameData.game().isInCheck(oppColor)){
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Because of the last move, %s is now in check".formatted(oppColor.toString()), gameData.gameID());
                announceNotification(session, notification, msg.getAuthToken(), "everyone");
            } else if (gameData.game().isInStalemate(oppColor)){
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "The game is in Stalemate. It's a tie!", gameData.gameID());
                gameData.game().setGameOver(true);
                announceNotification(session, notification, msg.getAuthToken(), "everyone");
            }

        } else{
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "It's not your turn", gameData.gameID());
            session.getRemote().sendString(new Gson().toJson(error));
        }
    }
//    private void leave(Session session, UserGameCommand msg) throws IOException {
//        try{
//            AuthData auth = Server.authDAO.getAuth(msg.getAuthToken());
//
//            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "%s has left the game".formatted(auth.username()));
//            announceNotification(session, notify, msg.getAuthToken(), "notUser");
//        } catch (DataAccessException | IOException e) {
//            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Not authorized");
//            session.getRemote().sendString(new Gson().toJson(error));
//        }
//    }
//
//    private void resign(Session session, UserGameCommand msg){
//        try{
//            AuthData auth = Server.authDAO.getAuth(msg.getAuthToken());
//            GameData gameData = Server.gameDAO.getGame(msg.getGameID());
//            ChessGame.TeamColor color = auth.username().equals(gameData.whiteUsername()) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
//
//            String oppUsername = color == ChessGame.TeamColor.WHITE? gameData.whiteUsername() : gameData.blackUsername();
//
//            if (gameData.game().isGameOver()){
//                ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Game is already over");
//                session.getRemote().sendString(new Gson().toJson(error));
//            }
//
//            gameData.game().setGameOver(true);
//            Server.gameDAO.updateGame(gameData);
//            ServerMessage notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "%s has resigned. %s wins!!".formatted(auth.username(), oppUsername));
//            announceNotification(session, notify, msg.getAuthToken(), "everyone");
//        } catch (DataAccessException | BadRequestException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void announceNotification(Session currSession, ServerMessage notification, String authToken, String audience) throws IOException {
        // to everyone, user, notUser
        for (var session: Server.sessions.keySet()){
            boolean in = Server.sessions.get(session) != 0;
            boolean same = Server.sessions.get(session).equals(Server.sessions.get(currSession));
            boolean toSelf = session == currSession;
            switch (audience) {
                case ("everyone"):
                    if (in) {
                        session.getRemote().sendString(new Gson().toJson(notification.getServerMessageType()));
                    }
                    break;
                case ("notUser"):
                    if (!toSelf) {
                        session.getRemote().sendString(new Gson().toJson(notification));
                    }
                    break;
                case ("user"):
                    if ((toSelf || same) && in) {
                        session.getRemote().sendString(new Gson().toJson(notification));
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
