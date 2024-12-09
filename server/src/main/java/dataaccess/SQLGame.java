package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;

public class SQLGame implements GameDAO{

    public SQLGame() {
        try{
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var conn = DatabaseManager.getConnection()){
            var createTable = """
                    CREATE TABLE if NOT EXISTS game(
                            gameID INT NOT NULL,
                            whiteUsername VARCHAR(255),
                            blackUsername VARCHAR(255),
                            gameName VARCHAR(255),
                            chessGame TEXT,
                            PRIMARY KEY (gameID)
                    )""";
            try (var statement = conn.prepareStatement(createTable)){
                statement.executeUpdate();

            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashSet<GameData> getGames() throws DataAccessException {
        HashSet<GameData> games = new HashSet<>();
        try (var conn =  DatabaseManager.getConnection()){
            try (var statement = conn.prepareStatement(
                    "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM game")) {
                try (var result = statement.executeQuery()) {
                    while (result.next()) {
                        var gameID = result.getInt(1);
                        var whiteUsername = result.getString(2);
                        var blackUsername = result.getString(3);
                        var gameName = result.getString(4);
                        var chessGame = new Gson().fromJson(result.getString("chessGame"), ChessGame.class);
                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return games;
    }

    @Override
    public int create(GameData game) throws DataAccessException {
        try (var conn =  DatabaseManager.getConnection()){
            try (var statement = conn.prepareStatement(
                    "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES(?,?,?,?,?)")) {
                    statement.setInt(1, game.gameID());
                    statement.setString(2, game.whiteUsername());
                    statement.setString(3, game.blackUsername());
                    statement.setString(4, game.gameName());
                    statement.setString(5, new Gson().toJson(game.game(), ChessGame.class));
                    statement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return game.gameID();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException, BadRequestException {
        try (var conn =  DatabaseManager.getConnection()){
            try (var statement = conn.prepareStatement(
                    "SELECT whiteUsername, blackUsername, gameName, chessGame FROM game WHERE gameID=?")) {
                statement.setInt(1, gameID);
                try (var result = statement.executeQuery()) {
                    result.next();
                    var whiteUsername = result.getString(1);
                    var blackUsername = result.getString(2);
                    var gameName = result.getString(3);
                    var chessGame = new Gson().fromJson(result.getString("chessGame"), ChessGame.class);
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                }
            }
        } catch (SQLException e){
            throw new BadRequestException(e.getMessage());

        } catch (DataAccessException e ) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        try (var conn =  DatabaseManager.getConnection()){
            try (var statement = conn.prepareStatement(
                    "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, chessGame=? WHERE gameID=?")) {
                statement.setString(1, game.whiteUsername());
                statement.setString(2, game.blackUsername());
                statement.setString(3, game.gameName());
                statement.setString(4, new Gson().toJson(game.game(), ChessGame.class));
                statement.setInt(5, game.gameID());
                if (statement.executeUpdate() == 0) {
                    throw new DataAccessException("game to be updated not found");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE game ")){
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e){
            throw new RuntimeException();
        }
    }
}
