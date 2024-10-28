package dataaccess;

import model.AuthData;

public class SQLAuth implements AuthDAO{
    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void add(AuthData data) {

    }

    @Override
    public void clear() {

    }
}
