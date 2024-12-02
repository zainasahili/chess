import client.ServerFacade;
import server.Server;
import ui.PreLogin;


public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(0);
        ServerFacade facade = new ServerFacade("localhost:" + port);
        System.out.println("Welcome to 240 chess. Type Help to get started.");
        PreLogin preLogin = new PreLogin(facade);
        preLogin.run();

    }
}