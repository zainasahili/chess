package ui;

import client.ServerFacade;

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
        System.out.println("Welcome to 240 chess. Type Help to get started.");

        while (!loggedIn) {
            String[] input = getInput();
            String command = input[0];
            switch (command) {
                case "help":
                    helpMenu();
                    break;
                case "quit":
                    return;
                case "login":
                    if (input.length != 3) {
                        System.out.println("please provide a username and password");
                        System.out.println("login <USERNAME> <PASSWORD> - login with an existing user");
                    } else if (server.login()) {
                        System.out.printf("Logged in as %s\n", input[1]);
                        loggedIn = true;
                    } else {
                        System.out.println("Invalid username/password");
                    }
                    break;
                case "register":
                    if (input.length != 4) {
                        System.out.println("please provide a username, password, and an email");
                        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
                    } else if (server.register()) {
                        server.login();
                        System.out.printf("Logged in as %s\n", input[1]);
                        loggedIn = true;
                    } else {
                        System.out.println("Username already in use");
                        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
                    }
                    break;
                default:
                    System.out.println("Command not recognized");
                    helpMenu();
                    break;
            }

        }
        postLogin.run();

    }

    private String[] getInput() {
        System.out.print("\n[LOGGED_OUT] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    public void helpMenu(){
        System.out.println("register «USERNAME> «PASSWORD> «EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }


}
