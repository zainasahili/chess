package pieces_rules;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;


public class Rookrules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        for (int i = -7; i <= 7; i++) {
            for (int j = -7; j <= 7; j++) {
                int row = myPosition.getRow() + i;
                int col = myPosition.getColumn() + j;
                if( row <= 8 && row >= 1 && 8 >= col && col >= 1 &&
                        (row == myPosition.getRow() || col == myPosition.getColumn()) ) {
                    ChessPosition newPosition = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null)
                        moves.add(new ChessMove(myPosition, newPosition, board.getPiece(myPosition).getPieceType()));

                    else{
                        if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor())
                            moves.add(new ChessMove(myPosition, newPosition, board.getPiece(myPosition).getPieceType()));
                        break;

                    }

                }

            }
        }
        return moves;
    }
}
