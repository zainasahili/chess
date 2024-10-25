package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GameServiceTest {

    static GameDAO gameDAO;
    static AuthDAO authDAO;
    static GameService gameService;
    static AuthData authData;

    @BeforeAll
    static void init(){
        gameDAO = new MemoryGame();
        authDAO = new MemoryAuth();
        gameService = new GameService(gameDAO, authDAO);

        authData = new AuthData("Token", "userName");
        authDAO.add(authData);
    }

    @BeforeEach
    void set(){
        gameDAO.clear();
    }
    @Test
    @DisplayName("Invalid listGames")
    void invalidListGames(){
        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames("ّInvalidToken"));
    }

    @Test
    @DisplayName("Valid listGames")
    void validListGames() throws DataAccessException {
        int game1 = gameService.createGame(authData.authToken(), "name");
        int game2 = gameService.createGame(authData.authToken(), "name");
        int game3 = gameService.createGame(authData.authToken(), "name");

        HashSet<GameData> expected = new HashSet<>();
        expected.add(new GameData(game1, null, null, "name", new ChessGame()));
        expected.add(new GameData(game2, null, null, "name", new ChessGame()));
        expected.add(new GameData(game3, null, null, "name", new ChessGame()));

        Assertions.assertEquals(expected, gameService.listGames(authData.authToken()));
    }

    @Test
    @DisplayName("Valid Create")
    void validCreateGame() throws DataAccessException {
        int game1 = gameService.createGame(authData.authToken(), "name");
        assertNotEquals(null, gameDAO.getGame(game1));

        int game2 = gameService.createGame(authData.authToken(), "name");
        assertNotEquals(gameDAO.getGame(game1), gameDAO.getGame(game2));

    }

    @Test
    @DisplayName("Invalid Create")
    void invalidCreateGame(){
        assertThrows(DataAccessException.class, () -> gameService.createGame("token", "name"));
    }

    @Test
    @DisplayName("Valid joinGame")
    void validJoinGame() throws DataAccessException, BadRequestException, TakenException {
        int gameID = gameService.createGame(authData.authToken(), "name");
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;

        gameService.joinGame(authData.authToken(), color, gameID);

        GameData expected = new GameData(gameID, authData.username(), null, "name", new ChessGame());

        Assertions.assertEquals(expected, gameDAO.getGame(gameID));
    }

    @Test
    @DisplayName("Invalid joinGame")
    void invalidJoinGame() throws DataAccessException {
        int gameID = gameService.createGame(authData.authToken(), "name");
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;

        assertThrows(DataAccessException.class, () -> gameService.joinGame("something", color, gameID));
        assertThrows(BadRequestException.class, () -> gameService.joinGame(authData.authToken(),
                                                    null, gameID));
    }

    @Test
    @DisplayName("Success clear")
    void clear() throws DataAccessException {
        gameService.createGame(authData.authToken(), "name");
        gameService.clear();
        assertEquals(gameService.listGames(authData.authToken()), new HashSet<>());
    }
}