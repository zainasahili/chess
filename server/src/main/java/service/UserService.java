package service;


import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

public class UserService {
        UserDAO userDAO;
        GameDAO gameDAO;
        AuthDAO authDAO;

    public UserService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }


//    public AuthData createUser(UserData user) {
//
//    }
//    public AuthData login(UserData user) {}
//    public void logout(AuthData auth) {}
    public void clear(){
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }
}
