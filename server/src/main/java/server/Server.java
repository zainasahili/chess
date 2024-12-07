package server;

import dataaccess.*;
import service.GameService;
import spark.*;
import service.UserService;
import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;


public class Server {


    UserDAO userDAO = new SQLUser();
    static GameDAO gameDAO = new SQLGame();
    static AuthDAO authDAO = new SQLAuth();

    UserService userService = new UserService(userDAO, authDAO);
    GameService gameService = new GameService(gameDAO, authDAO);
    Handler handler = new Handler(userService, gameService);


    static ConcurrentHashMap<Session, Integer> sessions = new ConcurrentHashMap<>();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", WebSocketHandler.class);
        Spark.post("/user", handler::register);
        Spark.post("/session", handler::login);
        Spark.delete("/session", handler::logout);
        Spark.get("/game", handler::listGames);
        Spark.post("/game", handler::createGame);
        Spark.put("/game", handler::joinGame);
        Spark.delete("/db", handler::clear);

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
