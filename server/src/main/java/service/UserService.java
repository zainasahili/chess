package service;


import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
        UserDAO userDAO;
        GameDAO gameDAO;
        AuthDAO authDAO;

    public UserService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
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
        authData = null;
        return authData;


    }
    public void logoutUser(String authToken) throws DataAccessException{

        authDAO.getAuth(authToken);


        authDAO.deleteAuth(authToken);


    }

    public void clear(){
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }
}
