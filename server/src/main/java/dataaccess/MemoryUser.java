package dataaccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUser implements UserDAO{

    HashSet<UserData> db;

    public MemoryUser() {
        db = HashSet.newHashSet(16);
    }

    @Override
    public void getUser(String username) throws DataAccessException {
        for (UserData user: db){
            if (user.username().equals(username)) {
                return;
            }
        }
        throw new DataAccessException("user not found" + username);
    }

    @Override
    public boolean authorized(String username, String password){
        for (UserData user: db) {
            if (user.username().equals(username) && user.password().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try{
            getUser(user.username());
        }
        catch (DataAccessException e){
            db.add(user);
            return;
        }

        throw new DataAccessException("user already exists");
    }

    @Override
    public void clear() {
        db = new HashSet<>();

    }
}
