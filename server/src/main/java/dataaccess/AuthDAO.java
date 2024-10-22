package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void deleteAuth(String authToken);
    AuthData getAuth(String authToken) throws DataAccessException;
    void add(AuthData data);
    void clear();
}
