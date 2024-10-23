package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.HashSet;
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

    public void joinGame(String authToken, String color, int gameID) throws DataAccessException{
        authDAO.getAuth(authToken);
        if (!gameDAO.exists(gameID))
            throw new DataAccessException("Game doesn't exist");

        // possibly change exists to getGame so we can use it to updateGame in the db?
        // also, change gameDAO.join to updateGame as I have it in the diagram?
    }
}
