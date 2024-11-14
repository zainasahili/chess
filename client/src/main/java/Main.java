import client.ServerFacade;
import server.Server;
import ui.PreLogin;


public class Main {
    static Server server = new Server();
    static int port = server.run(0);

    public static void main(String[] args) {

        ServerFacade facade = new ServerFacade("localhost:" + port);
        System.out.println("Welcome to 240 chess. Type Help to get started.");
        PreLogin preLogin = new PreLogin(facade);
        preLogin.run();
    }
}