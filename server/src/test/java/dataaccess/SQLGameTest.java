package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameTest {

    GameDAO gameDAO;
    GameData gameData;

    @BeforeEach
    void setup() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        gameDAO = new SQLGame();
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE game")){
                statement.executeUpdate();
            }
        }

        gameData = new GameData(123, "whiteUsername", "blackUsername", "name", new ChessGame());
    }

    @AfterEach
    void done() throws DataAccessException, SQLException{
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE game ")){
                statement.executeUpdate();
            }
        }
    }

    @Test
    void validGetGames() throws DataAccessException {
        gameDAO.create(gameData);
        gameDAO.create(new GameData(456, null, "black", "ggame", new ChessGame()));

        HashSet<GameData> expected = new HashSet<>();

        expected.add(gameData);
        expected.add(new GameData(456, null, "black", "ggame", new ChessGame()));
        Assertions.assertEquals(gameDAO.getGames(), expected);
    }

    @Test
    void invalidGetGames() throws DataAccessException {
        Assertions.assertEquals(0, gameDAO.getGames().size());
    }

    @Test
    void validCreate() throws DataAccessException, SQLException {
        gameDAO.create(gameData);
        GameData resultGame = null;

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
                        assertEquals(gameData.game(), chessGame);
                        resultGame = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    }
                }
            }
        }
        assertEquals(gameData, resultGame);

    }

    @Test
    void invalidCreate() throws DataAccessException {
        gameDAO.create(gameData);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.create(gameData));
    }

    @Test
    void validGetGame() throws DataAccessException, BadRequestException {
        gameDAO.create(gameData);
        Assertions.assertEquals(gameData, gameDAO.getGame(gameData.gameID()));

    }

    @Test
    void invalidGetGame() {
        Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.getGame(gameData.gameID()));
    }

    @Test
    void validUpdateGame() throws DataAccessException, BadRequestException {
        gameDAO.create(gameData);
        GameData newGame = new GameData(gameData.gameID(), "stop", "start", gameData.gameName(), new ChessGame());
        gameDAO.updateGame(newGame);

        Assertions.assertEquals(gameDAO.getGame(gameData.gameID()), newGame);
    }

    @Test
    void invalidUpdateGame() {
        GameData newGame = new GameData(gameData.gameID(), "stop", "start", gameData.gameName(), new ChessGame());
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(newGame));
    }

    @Test
    void clear() throws DataAccessException {
        gameDAO.create(gameData);
        gameDAO.clear();

        Assertions.assertEquals(0, gameDAO.getGames().size());
    }
}