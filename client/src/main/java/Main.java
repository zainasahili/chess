import client.ServerFacade;
import ui.PreLogin;

public class Main {
    public static void main(String[] args) {
        ServerFacade server = new ServerFacade();

        PreLogin preLogin = new PreLogin(server);
        preLogin.run();

    }
}