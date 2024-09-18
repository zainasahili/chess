package pieces_rules;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;
import java.util.Collection;


public class Queenrules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        for (int i = -7; i < 8; i++) {
            for (int j = -7; j < 8; j++) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;

                if (row >= 1 && row <= 8 && column >= 1 && column <= 8
                && (row - myPosition.getRow() == column - myPosition.getColumn()) ||
                        (row - myPosition.getRow() == 0) || (column - myPosition.getColumn() == 0)) {

                    ChessPosition newPosition = new ChessPosition(row, column);
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece != null || piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPosition, board.getPiece(myPosition).getPieceType()));
                    }
                }
            }
        }
        return moves;
    }
}
