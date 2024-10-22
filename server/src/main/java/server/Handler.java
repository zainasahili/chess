package server;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import javax.xml.crypto.Data;

public class Handler {


    static UserService userService;

    public Handler(UserService userService) {
        Handler.userService = userService;
    }

    public Object register(Request req, Response res) throws DataAccessException {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);

        if (userData.username() == null || userData.password() == null) {
//            return ("No username/password was given");
            res.status(400);
            return ("{ \"message\": \"Error: No username/password was given\" }");
        }

        try {
            AuthData authData = userService.createUser(userData);
            res.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            res.status(403);
            return ("{ \"message\": \"Error: username already taken\" }");
        }
    }

    public Object login(Request req, Response res) throws DataAccessException, BadRequestException{
        UserData userData = new Gson().fromJson(req.body(), UserData.class);

        AuthData authData = userService.loginUser(userData);

        if (authData != null) {
            res.status(200);
            return new Gson().toJson(authData);
        }
        res.status(401);
        return ("{ \"message\": \"Error: unauthorized\" }");
    }

    public Object logout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");

        try{
            userService.logoutUser(authToken);
        } catch (DataAccessException e) {
            res.status(401);
            return ("{ \"message\": \"Error: unauthorized\" }");
        }
        res.status(200);
        return "{}";

    }
}
