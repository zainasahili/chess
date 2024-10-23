package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
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

    public void joinGame(String authToken, String color, int gameID) throws DataAccessException{
        AuthData authData = authDAO.getAuth(authToken);
        GameData game = gameDAO.getGame(gameID);
        if ( game== null)
            throw new DataAccessException("Game doesn't exist");

        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        if (Objects.equals(color, "White")) {
            if(whiteUsername != null && !whiteUsername.equals(authData.username()))
                throw new DataAccessException("{ \"message\": \"Error: already taken\" }");
            whiteUsername = authData.username();
        }
        else if (Objects.equals(color, "Black")) {
            if (blackUsername != null && !blackUsername.equals(authData.username()))
                throw new DataAccessException("{ \"message\": \"Error: already taken\" }");
            blackUsername = authData.username();
        }
        try{
            gameDAO.updateGame(new GameData(gameID, whiteUsername, blackUsername, game.gameName(), game.game()));
        } catch (DataAccessException e){
            throw new DataAccessException(" { \" message \": \" Error: bad request \"} ");
        }
    }
}
