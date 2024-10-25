package server;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.util.HashSet;

public class Handler {


    static UserService userService;
    static GameService gameService;

    public Handler(UserService userService, GameService gameService) {
        Handler.userService = userService;
        Handler.gameService = gameService;
    }

    public Object register(Request req, Response res){
        UserData userData = new Gson().fromJson(req.body(), UserData.class);

        if (userData.username() == null || userData.password() == null) {
            res.status(400);
            return ("{ \"message\": \"Error: No username/password was given\" }");
        }

        try {
            AuthData authData = userService.createUser(userData);
            res.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            res.status(403);
            return ("{ \"message\": \"Error: already taken\" }");
        }
    }

    public Object login(Request req, Response res) throws DataAccessException{
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        AuthData authData = userService.loginUser(userData);

        if (authData != null) {
            res.status(200);
            return new Gson().toJson(authData);
        }
        res.status(401);
        return ("{ \"message\": \"Error: unauthorized\" }");
    }

    public Object logout(Request req, Response res) {
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

    public Object listGames(Request req, Response res) {
        String authToken = req.headers("authorization");
        HashSet<GameData> games;
        try {
            games = gameService.listGames(authToken);
        } catch (DataAccessException e) {
            res.status(401);
            return ("{ \"message\": \"Error: unauthorized\" }");
        }

        res.status(200);
        String result = new Gson().toJson(games);
        return "{\"games\": %s }".formatted(result);
    }

    public Object createGame(Request req, Response res){
        String authToken = req.headers("authorization");
        int gameID;
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        try{
            gameID = gameService.createGame(authToken, game.gameName());
        } catch (DataAccessException e){
            res.status(401);
            return ("{ \"message\": \"Error: unauthorized\" }");
        }

        res.status(200);
        return "{ \"gameID\": %d }".formatted(gameID);

    }
    public Object joinGame(Request req, Response res){
        String authToken = req.headers("authorization");
        record JoinGame(ChessGame.TeamColor playerColor, int gameID){}
        JoinGame game = new Gson().fromJson(req.body(), JoinGame.class);

        try{
            gameService.joinGame(authToken, game.playerColor(), game.gameID());
        } catch (DataAccessException e){
            res.status(401);
            return ("{ \"message\": \"Error: unauthorized\" }");
        } catch(BadRequestException e) {
            res.status(400);
            return("{ \"message\": \"Error: bad request\" }");
        } catch (TakenException e){
            res.status(403);
            return ("{ \"message\": \"Error: already taken\" }");
        }

        res.status(200);
        return "{}";

    }
    public Object clear(Request req, Response res){
        userService.clear();
        gameService.clear();

        res.status(200);
        return "{}";

    }
}
