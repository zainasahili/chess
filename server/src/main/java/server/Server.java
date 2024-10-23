package server;

import dataaccess.*;
import model.AuthData;
import service.GameService;
import spark.*;
import server.Handler;
import service.UserService;

import java.util.HashSet;

public class Server {

    HashSet<AuthData> db = new HashSet<>();

    UserDAO userDAO = new MemoryUser();
    GameDAO gameDAO = new MemoryGame();
    AuthDAO authDAO = new MemoryAuth();

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
        Spark.delete("/db", this::clear);
        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();



        Spark.awaitInitialization();
        return Spark.port();
    }



    private Object clear(Request req, Response res){
        res.status(200);
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
