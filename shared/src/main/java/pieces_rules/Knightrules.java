package pieces_rules;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.abs;


public class Knightrules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                int row = myPosition.getRow() + i;
                int col= myPosition.getColumn() + j;
                if ((row >= 1 && row <= 8 && col >= 1 && col <= 8) &&
                        (abs(col - myPosition.getColumn()) == 2 && abs(row - myPosition.getRow()) == 1) ||
                        (abs(col - myPosition.getColumn()) == 1 && abs(row - myPosition.getRow()) == 2)){
                    ChessPosition newPosition = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(newPosition);
                    if(piece == null || piece.getTeamColor() != color){
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }
        return moves;
    }
}
