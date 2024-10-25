package service;


import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
        UserDAO userDAO;
        AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }


    public AuthData createUser(UserData user) throws DataAccessException {
        try {
            userDAO.createUser(user);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        authDAO.add(authData);

        return authData;
    }
    public AuthData loginUser(UserData user) throws DataAccessException {
        boolean authorized;
        AuthData authData;

        authorized = userDAO.authorized(user.username(), user.password());

        if (authorized){
            String authToken = UUID.randomUUID().toString();
            authData = new AuthData(authToken, user.username());
            authDAO.add(authData);
            return authData;
        }
        return null;

    }
    public void logoutUser(String authToken) throws DataAccessException{
        try {
            authDAO.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
        authDAO.deleteAuth(authToken);
    }

    public void clear(){
        userDAO.clear();
        authDAO.clear();
    }
}
