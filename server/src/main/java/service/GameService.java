package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;


    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public HashSet<GameData> listGames(String authToken) throws DataAccessException {
        authDAO.getAuth(authToken);

        return gameDAO.getGames();
    }

    public int createGame(String authToken, GameData game) throws DataAccessException {
        authDAO.getAuth(authToken);
        int gameID = new Random().nextInt(1000);

        return gameDAO.create(new GameData(gameID, null, null, game.gameName(), new ChessGame()));
    }

    public void joinGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException, BadRequestException, TakenException {
    AuthData authData = authDAO.getAuth(authToken);
    GameData game = gameDAO.getGame(gameID);

    if (game == null)
        throw new BadRequestException("Game doesn't exist");
    else if (color == null)
        throw new BadRequestException("{ \"message\": \"Error: bad request\" }");
    else if (Objects.equals(color, ChessGame.TeamColor.WHITE)) {
        if(game.whiteUsername() != null)
            throw new TakenException();
        gameDAO.updateGame(new GameData(gameID, authData.username(), game.blackUsername(), game.gameName(), game.game()));
    }
    else if (Objects.equals(color, ChessGame.TeamColor.BLACK)) {
        if (game.blackUsername() != null)
            throw new TakenException();
        gameDAO.updateGame(new GameData(gameID, game.whiteUsername(), authData.username(), game.gameName(), game.game()));
        }
    }

    public void clear(){
        gameDAO.clear();
    }
}
