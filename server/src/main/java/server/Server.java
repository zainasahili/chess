package server;

import dataaccess.*;
import model.AuthData;
import service.GameService;
import spark.*;
import server.Handler;
import service.UserService;

import java.util.HashSet;

public class Server {

//    HashSet<AuthData> db = new HashSet<>();

    UserDAO userDAO = new SQLUser();
    GameDAO gameDAO = new SQLGame();
    AuthDAO authDAO = new SQLAuth();

    UserService userService = new UserService(userDAO, authDAO);
    GameService gameService = new GameService(gameDAO, authDAO);
    Handler handler = new Handler(userService, gameService);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", handler::register);
        Spark.post("/session", handler::login);
        Spark.delete("/session", handler::logout);
        Spark.get("/game", handler::listGames);
        Spark.post("/game", handler::createGame);
        Spark.put("/game", handler::joinGame);
        Spark.delete("/db", handler::clear);
        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();



        Spark.awaitInitialization();
        return Spark.port();
    }

    public void clear(){
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
