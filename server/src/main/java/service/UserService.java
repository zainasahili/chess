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


    public AuthData createUser(UserData user) throws BadRequestException {
        try {
            userDAO.createUser(user);
        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        authDAO.add(authData);

        return authData;
    }
//    public AuthData login(UserData user) {}
//    public void logout(AuthData auth) {}
    public void clear(){
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }
}
