package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    boolean authorized(String username, String password) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    void clear();
}
