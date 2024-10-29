package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthTest {

    AuthDAO authDAO;
    AuthData auth;

    @BeforeEach
    void setup() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        authDAO = new SQLAuth();
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE auth ")){
                statement.executeUpdate();
            }
        }

        auth = new AuthData("authToken", "username");
    }

    @AfterEach
    void done() throws DataAccessException, SQLException{
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE auth ")){
                statement.executeUpdate();
            }
        }
    }

    @Test
    void validDeleteAuth() throws DataAccessException, SQLException {
        authDAO.add(auth);
        authDAO.deleteAuth(auth.authToken());

        try (var conn =  DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT authToken, username FROM auth WHERE authToken=?")) {
                statement.setString(1, auth.authToken());
                try (var result = statement.executeQuery()) {
                    Assertions.assertFalse(result.next());
                }
            }
        }
    }

    @Test
    void invalidDeleteAuth() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(auth.authToken()));
    }

    @Test
    void validGetAuth() throws DataAccessException {
        authDAO.add(auth);
        AuthData result = authDAO.getAuth(auth.authToken());
        Assertions.assertEquals(auth, result);
    }

    @Test
    void invalidGetAuth() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth(auth.authToken()));
    }

    @Test
    void validAdd() throws DataAccessException, SQLException {
        authDAO.add(auth);
        String username;
        String authToken;

        try (var conn =  DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT authToken, username FROM auth WHERE authToken=?")) {
                statement.setString(1, auth.authToken());
                try (var result = statement.executeQuery()) {
                    result.next();
                    authToken = result.getString(1);
                    username = result.getString(2);

                }
            }
        }

        Assertions.assertEquals(auth.username(), username);
        Assertions.assertEquals(auth.authToken(), authToken);

    }

    @Test
    void invalidAdd() throws DataAccessException {
        authDAO.add(auth);
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.add(auth));
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        authDAO.add(auth);
        authDAO.clear();

        try (var conn =  DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT authToken, username FROM auth WHERE authToken=?")) {
                statement.setString(1, auth.authToken());
                try (var result = statement.executeQuery()) {
                    Assertions.assertFalse(result.next());
                }
            }
        }
    }
}