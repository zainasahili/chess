package ui;


import chess.*;

import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class BoardLayout {

    ChessGame game;

    public BoardLayout(ChessGame game) {
        this.game = game;
    }

    public void printBoard(ChessGame.TeamColor color, ChessPosition position){
        StringBuilder out = new StringBuilder();

        boolean reversed = color == ChessGame.TeamColor.BLACK;
        Collection<ChessMove> possibles = position != null? game.validMoves(position): null;
        HashSet<ChessPosition> squares = HashSet.newHashSet(possibles != null ? possibles.size() : 0);
        if (possibles != null){
            for (ChessMove move: possibles){
                squares.add(move.getEndPosition());
            }
        }
        int count = color == null? 2 : 1;
        for (int  i = 0; i < count; i++){

            out.append(printHeader(reversed));
            out.append("\n");

            for (int x = 8; x > 0; x--){
                int row = reversed? x : (x * -1) + 9;
                out.append(printRow(reversed, row, position, squares));
            }

            out.append(printHeader(reversed));
            out.append("\n");
            if (i < 1) {
                out.append("\n");
            }
            reversed = !reversed;
            out.append(RESET_TEXT_BOLD_FAINT);
            out.append(RESET_TEXT_COLOR);
        }

        System.out.println(out);

    }

    public  String printHeader(boolean reversed){
        String header = reversed? "    a  b  c  d  e  f  g  h    " : "    h  g  f  e  d  c  b  a    ";
        String out = SET_BG_COLOR_LIGHT_GREY +
                SET_TEXT_COLOR_BLACK +
                header +
                RESET_BG_COLOR;
        return out;

    }
    public  String printRow(boolean reversed, int row, ChessPosition position, HashSet<ChessPosition> squares){
        StringBuilder out = new StringBuilder();
        out.append(SET_BG_COLOR_LIGHT_GREY);
        out.append(SET_TEXT_COLOR_BLACK);
        out.append(" %d ".formatted(row));

        for (int i = 1; i < 9; i++){
            int col = reversed? i : (i * -1) + 9;
            out.append(squareColor(row, col, position, squares));
            out.append(getPiece(row, col));
        }

        out.append(SET_BG_COLOR_LIGHT_GREY);
        out.append(SET_TEXT_COLOR_BLACK);
        out.append(" %d ".formatted(row));
        out.append(RESET_BG_COLOR);
        out.append(RESET_TEXT_COLOR);
        out.append("\n");

        return out.toString();
    }

    private String squareColor(int row, int col, ChessPosition position ,HashSet<ChessPosition> squares){
        ChessPosition current = new ChessPosition(row, col);
        if (current.equals(position)){
            return SET_BG_COLOR_BLUE;
        } else if (squares.contains(current)){
            return SET_BG_COLOR_MAGENTA;
        } if (row % 2 == 0){
            if (col % 2 == 0){
                return SET_BG_COLOR_BLACK;
            } else{
                return SET_BG_COLOR_WHITE;
            }
        } else {
            if (col % 2 != 0){
                return SET_BG_COLOR_BLACK;
            } else{
                return SET_BG_COLOR_WHITE;
            }
        }
    }

    public  String getPiece(int row, int col){
        StringBuilder out = new StringBuilder();
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = game.getBoard().getPiece(position);
        if (piece != null) {
            out.append(SET_TEXT_BOLD);
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.append(SET_TEXT_COLOR_RED);
            } else {
                out.append(SET_TEXT_COLOR_BLUE);
            }

            switch (piece.getPieceType()) {
                case KING -> out.append(" K ");
                case QUEEN -> out.append(" Q ");
                case BISHOP -> out.append(" B ");
                case KNIGHT -> out.append(" N ");
                case ROOK -> out.append(" R ");
                case PAWN -> out.append(" P ");
            }
            out.append(RESET_TEXT_BOLD_FAINT);
        } else {
            out.append("   ");
        }

        return out.toString();
    }
}
