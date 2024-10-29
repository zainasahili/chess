package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUser implements UserDAO{


    public SQLUser(){
        try{
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var conn = DatabaseManager.getConnection()){
            var createTable = """
                    CREATE TABLE if NOT EXISTS user(
                            username VARCHAR(255) NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            email VARCHAR(255),
                            PRIMARY KEY (username)
                    )""";
            try (var statement = conn.prepareStatement(createTable)){
                statement.executeUpdate();

            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
            try (var conn =  DatabaseManager.getConnection()){
                try (var statement = conn.prepareStatement("SELECT username, password, email FROM user WHERE username=?")) {
                    statement.setString(1, username);
                    try (var result = statement.executeQuery()) {
                        result.next();
                        var password = result.getString(2);
                        var email = result.getString(3);
                        return new UserData(username, password, email);
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException("user not found" + username);
            }
    }

    @Override
    public boolean authorized(String username, String password) throws DataAccessException{
        UserData user = getUser(username);
        return BCrypt.checkpw(password, user.password());

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?,?,?)")){
                statement.setString(1, user.username());
                String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
                statement.setString(2, hashedPassword);
                statement.setString(3, user.email());
                statement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("User already exists");
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE user ")){
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e){
            throw new RuntimeException();
        }
    }
}
