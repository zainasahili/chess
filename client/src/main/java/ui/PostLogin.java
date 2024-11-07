package ui;

import client.ServerFacade;
import model.GameData;

import java.util.Collection;
import java.util.HashSet;
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
                    server.logout();
                    loggedIn = false;
                    break;
                case "createGame":
                    if (input.length != 2){
                        System.out.println("Choose a name for the game");
                        System.out.println("create ‹NAME>");
                    } else if (server.createGame(input[1])){
                        System.out.printf("%s game create", input[1]);
                        break;
                    }
            }

        }
    }

    private String[] getInput() {
        System.out.print("\n[LOGGED_IN] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    public String helpMenu(){
        return null;
    }

    public void logout(){}

    public boolean createGame(){
        return true;
    }

    public Collection<GameData> listGames(){
        return HashSet.newHashSet(5);
    }

    public void playGame(){

    }

    public void observeGame(){}

}
