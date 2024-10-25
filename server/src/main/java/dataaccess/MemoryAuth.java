package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemoryAuth implements AuthDAO{

    HashSet<AuthData> db;

    public MemoryAuth() {
       db = HashSet.newHashSet(16);
    }


    @Override
    public void deleteAuth(String authToken) {
        for (AuthData data: db) {
            if (data.authToken().equals(authToken)) {
                db.remove(data);
                break;
            }
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        for (AuthData data: db) {
            if (data.authToken().equals(authToken)) {
                return data;
            }
        }

        throw new DataAccessException("{ \"message\": \"Error: unauthorized\" }");
    }

    @Override
    public void add(AuthData data) {
            db.add(data);
    }

    @Override
    public void clear() {
        db = new HashSet<>();

    }
}
