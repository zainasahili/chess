package server;

import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import java.awt.*;
import java.io.IOException;

public class WebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session session){
        Server.sessions.put((spark.Session) session, 0);
    }

    @OnWebSocketClose
    public void onClose(Session session){
        Server.sessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, BadRequestException, IOException, InvalidMoveException {
        UserGameCommand msg = new Gson().fromJson(message, UserGameCommand.class);
        if (msg.getCommandType().equals(UserGameCommand.CommandType.MAKE_MOVE)){
            makeMove(session, msg);
        } else if (msg.getCommandType().equals(UserGameCommand.CommandType.LEAVE)){
            leave(session, msg);
        } else if (msg.getCommandType().equals(UserGameCommand.CommandType.RESIGN)){
            resign(session, msg);
        }
    }
    private void makeMove(Session session, UserGameCommand msg) throws DataAccessException, BadRequestException, IOException, InvalidMoveException {
        AuthData authData = Server.authDAO.getAuth(msg.getAuthToken());
        GameData gameData = Server.gameDAO.getGame(msg.getGameID());
        ChessGame.TeamColor color =  authData.username().equals(gameData.whiteUsername()) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

        if (gameData.game().isGameOver()){
            Error error = new Error("Game is over");
            System.out.printf("Error: %s", new Gson().toJson(error));
            session.getRemote().sendString(new Gson().toJson(error));
        } else if (gameData.game().getTeamTurn().equals(color)){
            gameData.game().makeMove(msg.getMove());

            ServerMessage notification;
            ChessGame.TeamColor oppColor = color == ChessGame.TeamColor.WHITE? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

            if (gameData.game().isInCheckmate(oppColor)){
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Checkmate! %s wins".formatted(authData.username()));
                gameData.game().setGameOver(true);
            } else if (gameData.game().isInCheck(oppColor)){
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Because of the last move, %s is now in check".formatted(oppColor.toString()));
            } else if (gameData.game().isInStalemate(oppColor)){
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "The game is in Stalemate. It's a tie!");
                gameData.game().setGameOver(true);
            } else{
                notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, " a move has been made by %s".formatted(authData.username()));
            }
            announceNotification(session, notification);
            Server.gameDAO.updateGame(gameData);

        }
    }
    private void announceNotification(Session session, ServerMessage notification){

    }
    private void leave(Session session, UserGameCommand msg){}

    private void resign(Session session, UserGameCommand msg){}
}
