package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

class SQLUserTest {

    UserDAO userDAO;
    UserData user;

    @BeforeEach
    void setup() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        userDAO = new SQLUser();
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE user ")){
                statement.executeUpdate();
            }
        }

        user = new UserData("username", "password", "email");
    }

    @AfterEach
    void done() throws DataAccessException, SQLException{
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE user ")){
                statement.executeUpdate();
            }
        }
    }

    @Test
    void validGetUser() throws DataAccessException {
        userDAO.createUser(user);
        UserData result = userDAO.getUser(user.username());

        Assertions.assertEquals(user.username(), result.username());
        Assertions.assertTrue(BCrypt.checkpw(user.password(), result.password()));
        Assertions.assertEquals(user.email(), result.email());

    }

    @Test
    void invalidGetUser() {
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.getUser(user.username()));
    }

    @Test
    void validAuthorized() throws DataAccessException {
        userDAO.createUser(user);
        Assertions.assertTrue(userDAO.authorized(user.username(), user.password()));
    }

    @Test
    void invalidAuthorized() throws DataAccessException {
        userDAO.createUser(user);
        Assertions.assertFalse(userDAO.authorized(user.username(), "notIT"));
    }

    @Test
    void validCreateUser() throws DataAccessException, SQLException {
        userDAO.createUser(user);
        String username;
        String password;
        String email;

        try (var conn =  DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, password, email FROM user WHERE username=?")) {
                statement.setString(1, user.username());
                try (var result = statement.executeQuery()) {
                    result.next();
                    username = result.getString(1);
                    password = result.getString(2);
                    email = result.getString(3);
                }
            }
        }

        Assertions.assertEquals(user.username(), username);
        Assertions.assertTrue(BCrypt.checkpw(user.password(), password));
        Assertions.assertEquals(user.email(), email);
    }

    @Test
    void invalidCreateUser() throws DataAccessException {
        userDAO.createUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        userDAO.createUser(user);
        userDAO.clear();

        try (var conn =  DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT username, password, email FROM user WHERE username=?")) {
                statement.setString(1, user.username());
                try (var result = statement.executeQuery()) {
                    Assertions.assertFalse(result.next());
                }
            }
        }
    }
}