package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

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

        gameData = new GameData(123, "whiteUsername", null, "name", new ChessGame());
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
    void validGetGames() {
    }

    @Test
    void invalidGetGames() {
    }
    @Test
    void create() {
    }

    @Test
    void getGame() {
    }

    @Test
    void updateGame() {
    }

    @Test
    void clear() {
    }
}