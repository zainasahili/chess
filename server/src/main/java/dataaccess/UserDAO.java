package dataaccess;

import model.UserData;

public interface UserDAO {

    void createUser(UserData user) throws DataAccessException;
    void clear();
}
