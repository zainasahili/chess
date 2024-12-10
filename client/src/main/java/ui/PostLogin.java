package ui;

import chess.ChessGame;
import client.ServerFacade;
import model.GameData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class PostLogin {

    ServerFacade facade;
    Collection<GameData> games;

    public PostLogin(ServerFacade facade) {
        this.facade = facade;
    }

    public void run() throws IOException {
        boolean loggedIn = true;

        while (loggedIn){
            String[] input = getInput();
            String command = input[0];
            switch (command){
                case "help":
                    helpMenu();
                    break;
                case "logout":
                    if (facade.logout()){
                        loggedIn = false;
                    }
                    break;
                case "create":
                    if (input.length != 2) {
                        System.out.println("Choose a name for the game");
                        System.out.println("create ‹NAME>");
                        break;
                    }
                    facade.createGame(input[1]);
                    System.out.printf("%s game created\n", input[1]);
                    break;
                case "list":
                    games = facade.listGames();
                    int i = 1;
                    for (GameData game: games){
                        System.out.printf("%d. Game: %s, WhitePlayer: %s, BlackPlayer: %s\n",
                                i, game.gameName(), game.whiteUsername(), game.blackUsername());
                        i++;
                    }
                    break;
                case "join":
                    if (input.length != 3 || !input[1].matches("\\d{1,2}") || !input[2].toUpperCase().matches("WHITE|BLACK")){
                        System.out.println("Choose a game ID and player color");
                        System.out.println("join <ID> [WHITE|BLACK]");
                    }
                    else {
                        games = facade.listGames();
                        List<GameData> result = new ArrayList<>(games);
                        GameData game = result.get(Integer.parseInt(input[1])-1);
                        ChessGame.TeamColor color = input[2].equalsIgnoreCase("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                        if (facade.joinGame(color, game.gameID())){
                            System.out.println("You have joined the game");
                            facade.connectToWs();
                            BoardLayout.team = color;
                            facade.joinPlayer(game.gameID(), color);
                            GamePlay gamePlay = new GamePlay(facade, game, color);
                            gamePlay.run();
                        }
                        else{
                            System.out.println("Color taken or game doesn't exist");
                        }
                    }
                    break;
                case "observe":
                    if (input.length != 2 || !input[1].matches("\\d")){
                        System.out.println("Choose a game ID");
                        System.out.println("observe ‹ID>");
                    }
                    else {
                        games = facade.listGames();
                        List<GameData> result = new ArrayList<>(games);
                        GameData game = result.get(Integer.parseInt(input[1])-1);
                        facade.connectToWs();
                        facade.observe(game.gameID());
                        GamePlay gamePlay = new GamePlay(facade, game);
                        gamePlay.run();
                    }
                    break;
                default:
                    System.out.println("Command not recognized");
                    helpMenu();
                    break;
            }
        }

        PreLogin preLogin = new PreLogin(facade);
        preLogin.run();


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
        System.out.println("help - with possible commands");
    }




}
