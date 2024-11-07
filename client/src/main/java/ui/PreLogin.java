package ui;

import client.ServerFacade;

import java.util.Objects;
import java.util.Scanner;

public class PreLogin {

    ServerFacade server;
    PostLogin postLogin;

    public PreLogin(ServerFacade server) {
        this.server = server;
        postLogin = new PostLogin(server);
    }

    public void run(){
        boolean loggedIn = false;
        System.out.print("Welcome to 240 chess. Type Help to get started.");

        while (!loggedIn) {
            String[] input = getInput();
            String function = input[0];
            if (Objects.equals(function, "help")){
                help();
                break;
            }
            else if (Objects.equals(function, "quit")){
                return;
            }
            else if (Objects.equals(function, "login")){
                login();
                break;
            }
            else if (Objects.equals(function, "register")){
                register();
                break;
            }
        }

    }

    private String[] getInput() {
        System.out.print("\n[LOGGED_OUT] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    public String help(){
        return null;
    }

    public boolean login(){
        return true;
    }

    public boolean register(){
        return true;
    }

}
