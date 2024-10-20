package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.post("/user", this::register);

        Spark.awaitInitialization();
        return Spark.port();
    }
    private Object register(Request req, Response res){
        return """
                { "username":"", "password":"", "email":"" }
                """;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
