package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void add(AuthData data);
    void clear();
}
