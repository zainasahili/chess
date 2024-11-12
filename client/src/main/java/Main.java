import client.ServerFacade;
import ui.PreLogin;

import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(0);
        ServerFacade facade = new ServerFacade("localhost:" + port);

        PreLogin preLogin = new PreLogin(facade);
        preLogin.run();

    }
}