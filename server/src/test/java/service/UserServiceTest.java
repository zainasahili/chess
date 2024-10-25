package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


class UserServiceTest {

    static UserDAO userDAO;
    static AuthDAO authDAO;
    static UserService userService;
    static UserData userData;

    @BeforeAll
    static void init(){
        userDAO = new MemoryUser();
        authDAO = new MemoryAuth();
        userService = new UserService(userDAO, authDAO);

    }

    @BeforeEach
    void set(){
        userDAO.clear();
        authDAO.clear();

        userData = new UserData("username", "password", "username@gmail.com");
    }

    @Test
    @DisplayName("Valid create")
    void validCreateUser() throws DataAccessException {
        AuthData data = userService.createUser(userData);
        Assertions.assertEquals(authDAO.getAuth(data.authToken()), data);

    }

    @Test
    @DisplayName("Invalid create")
    void invalidCreateUser() throws DataAccessException {
        userService.createUser(userData);
        assertThrows(DataAccessException.class, () -> userService.createUser(userData));
    }

    @Test
    @DisplayName("Valid loginUser")
    void validLoginUser() throws DataAccessException {
        userService.createUser(userData);
        AuthData authData = userService.loginUser(userData);
        Assertions.assertEquals(authDAO.getAuth(authData.authToken()), authData);
    }

    @Test
    @DisplayName("Invalid loginUser")
    void invalidLoginUser() throws DataAccessException {
        assertNull(userService.loginUser(userData));
    }

    @Test
    @DisplayName("Valid logoutUser")
    void validLogoutUser() throws DataAccessException {
        userService.createUser(userData);
        AuthData authData = userService.loginUser(userData);
        userService.logoutUser(authData.authToken());
        assertThrows(DataAccessException.class, () -> authDAO.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("Invalid logoutUser")
    void invalidLogoutUser() throws DataAccessException {
        AuthData authData = userService.createUser(userData);
        userService.logoutUser(authData.authToken());
        assertThrows(DataAccessException.class, () -> userService.logoutUser(authData.authToken()));
    }

    @Test
    void clear() throws DataAccessException {
        userService.createUser(userData);
        userService.clear();
        assertThrows(DataAccessException.class, () -> userDAO.getUser(userData.username()));


    }
}