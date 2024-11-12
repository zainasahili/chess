package ui;

import client.ServerFacade;
import java.util.Scanner;

public class PostLogin {

    ServerFacade server;

    public PostLogin(ServerFacade server) {
        this.server = server;
    }

    public void run(){
        boolean loggedIn = true;

        while (loggedIn){
            String[] input = getInput();
            String command = input[0];
            switch (command){
                case "help":
                    helpMenu();
                    break;
                case "logout":
                    if (server.logout()){
                    loggedIn = false;}
                    break;
                case "create":
                    if (input.length != 2){
                        System.out.println("Choose a name for the game");
                        System.out.println("create ‹NAME>");
                    } else if (server.createGame(input[1])){
                        System.out.printf("%s game created\n", input[1]);
                    }
                    break;
                case "list":
                    System.out.printf("%s", server.listGame());
                    break;
                case "join":
                    if (input.length != 3){
                        System.out.println("Choose a game ID and player color");
                        System.out.println("join <ID> [WHITE|BLACK]");
                    } else {
                        server.joinGame();
                    }
                    break;
                case "observe":
                    if (input.length != 2){
                        System.out.println("Choose a game ID");
                        System.out.println("observe ‹ID>");
                    } else{
                        server.observeGame();
                    }
                    break;
                default:
                    System.out.println("Command not recognized");
                    helpMenu();
                    break;
            }

        }
    }

    private String[] getInput() {
        System.out.print("\n[LOGGED_IN] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    public void helpMenu(){
        System.out.println("create ‹NAME> - a game");
        System.out.println("list - games");
        System.out.println("join <ID> [WHITE|BLACK] - a game");
        System.out.println("observe ‹ID> - a game");
        System.out.println("logout - when you are done");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }




}
