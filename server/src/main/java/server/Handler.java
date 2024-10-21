package server;

import dataaccess.BadRequestException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class Handler {

    static UserService userService;

    public Handler(UserService userService) {
        this.userService = userService;
    }

    public static Object register(Request req, Response res) throws BadRequestException {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);

        if (userData.username() == null || userData.password() == null)
            throw new BadRequestException("No username/password was given");

        AuthData authData = userService.createUser(userData);
        res.status(200);
        return new Gson().toJson(userData);
    }
}
