package server;

import dataaccess.AuthDAO;
import dataaccess.BadRequestException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
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

    public Object register(Request req, Response res) throws BadRequestException {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);

        if (userData.username() == null || userData.password() == null)
            throw new BadRequestException("No username/password was given");

        try {
            AuthData authData = Handler.userService.createUser(userData);
            res.status(200);
            return new Gson().toJson(authData);
        } catch (BadRequestException e) {
            res.status(403);
            throw new RuntimeException("{ \"message\": \"Error: already taken\" }");
        }
    }
}
