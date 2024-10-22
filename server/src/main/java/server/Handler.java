package server;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

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
}
