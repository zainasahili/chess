package ui;

import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;

import java.util.Objects;
import java.util.Scanner;

public class GamePlay {

    ServerFacade facade;
    BoardLayout boardLayout;

    public GamePlay(ServerFacade facade) {
        this.facade = facade;
    }


    public void run(){
        boolean in = true;
        while (in){
            String[] input = getInput();
            String command = input[0];
            switch (command) {
                case "help":
                    helpMenu();
                    break;
                case "redraw":
                    boardLayout.printBoard();
                    break;
                case "leave":
                    in = false;
                    facade.leaveGame();
                    break ;
                case "make move":
                    makeMove(input);
                    break;
                case "resign":
                    System.out.println("Are you sure you want to resign? (yes/no)");
                    String[] answer = getInput();
                    if (answer.length == 1 && Objects.equals(answer[0], "yes")) {
                        facade.resign();
                    } else {
                        System.out.println("Resignation cancelled");
                    }
                    break;
                case "highlight":
                    if (input.length == 2 && input[1].matches("[a-h][1-8]")) {
                        boardLayout.printBoard();
                    } else {
                        System.out.println("Please provide coordinates");
                        System.out.println("Highlight <coordinates> (ex: b3) - highlight all legal moves for the piece");
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
        System.out.print("\n[IN_GAME] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }
    private void helpMenu(){
        System.out.println("redraw - redraw the Chess Board");
        System.out.println("leave - leave the game");
        System.out.println("move <from> <to> <promotion piece> - make a move (promotion piece should only be used if a move will promote a pawn)");
        System.out.println("resign - resign from a game");
        System.out.println("highlight <coordinates> - highlight all legal moves for the piece");
    }
    private void makeMove(String[] input){
        if (input.length >= 3 && input[1].matches("[a-h][1-8]") && input[2].matches("[a-h][1-8]")){
            ChessPosition from = new ChessPosition(input[1].charAt(1), input[1].charAt(0) - ('a'-1));
            ChessPosition to = new ChessPosition(input[2].charAt(1), input[2].charAt(0) - ('a'-1));
            ChessPiece.PieceType promotion = null;
            if (input.length == 4){
                promotion = getPieceType(input[3]);
                if (promotion == null){
                    System.out.println("Invalid promotion piece. Please provide promotion name. ex: knight");
                    System.out.println("move <from> <to> <promotion piece> - make a move (promotion piece should only be used if a move will promote a pawn)");
                }
            }
            facade.makeMove();
        } else{
            System.out.println("please provide from and to coordinates. ex: 4a 5c");
            System.out.println("move <from> <to> <promotion piece> - make a move (promotion piece should only be used if a move will promote a pawn)");
        }
    }
    private ChessPiece.PieceType getPieceType(String piece){
        return switch (piece.toUpperCase()){
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "PAWN" -> ChessPiece.PieceType.PAWN;
            default -> null;
        };
    }

}
