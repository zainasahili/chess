package ui;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardLayout {

    ChessBoard board;

    public BoardLayout(ChessBoard board) {
        this.board = board;
    }

    public void printBoard(){
        StringBuilder out = new StringBuilder();

        boolean reversed = true;

        for (int  i = 0; i < 2; i++){

            out.append(printHeader(reversed));
            out.append("\n");

            for (int x = 8; x > 0; x--){
                int row = reversed? x : (x * -1) + 9;
                out.append(printRow(reversed, row));
            }

            out.append(printHeader(reversed));
            out.append("\n");
            if (i < 1) {
                out.append("\n");
            }
            reversed = false;
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
    public  String printRow(boolean reversed, int row){
        StringBuilder out = new StringBuilder();
        out.append(SET_BG_COLOR_LIGHT_GREY);
        out.append(SET_TEXT_COLOR_BLACK);
        out.append(" %d ".formatted(row));

        for (int i = 1; i < 9; i++){
            int col = reversed? i : (i * -1) + 9;
            if (row % 2 == 0){
                if (col % 2 == 0){
                out.append(SET_BG_COLOR_BLACK);
                } else{
                    out.append(SET_BG_COLOR_WHITE);
                }
            } else {
                if (col % 2 != 0){
                    out.append(SET_BG_COLOR_BLACK);
                } else{
                    out.append(SET_BG_COLOR_WHITE);
                }
            }
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

    public  String getPiece(int row, int col){
        StringBuilder out = new StringBuilder();
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);
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
