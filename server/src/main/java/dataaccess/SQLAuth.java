package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public class SQLAuth implements AuthDAO{

    public SQLAuth() {
        try{
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try (var conn = DatabaseManager.getConnection()){
            var createTable = """
                    CREATE TABLE if NOT EXISTS auth(
                            username VARCHAR(255) NOT NULL,
                            authToken VARCHAR(255) NOT NULL,
                            PRIMARY KEY (authToken)
                    )""";
            try (var statement = conn.prepareStatement(createTable)){
                statement.executeUpdate();

            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("DELETE FROM auth WHERE authToken=? ")) {
                    statement.setString(1, authToken);
                    statement.executeUpdate();
            }
        }catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn =  DatabaseManager.getConnection()){
            try (var statement = conn.prepareStatement("SELECT username, authToken FROM auth WHERE authToken=?")) {
                statement.setString(1, authToken);
                try (var result = statement.executeQuery()) {
                    result.next();
                    var username = result.getString(2);
                    return new AuthData(username, authToken);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("authToken not found" + authToken);
        }
    }

    @Override
    public void add(AuthData data) {
        try (var conn =  DatabaseManager.getConnection()){
            try (var statement = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES(?,?)")) {
                statement.setString(1, data.authToken());
                statement.setString(2, data.username());
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {}
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()){
            try(var statement = conn.prepareStatement("TRUNCATE auth ")){
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e){
            throw new RuntimeException();
        }
    }
}
