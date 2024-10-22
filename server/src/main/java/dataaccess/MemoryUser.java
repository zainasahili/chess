package dataaccess;

import model.UserData;

import java.util.HashSet;

public class MemoryUser implements UserDAO{

    HashSet<UserData> db = new HashSet<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user: db){
            if (user.username().equals(username))
                return user;
        }
        throw new DataAccessException("user not found" + username);
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
