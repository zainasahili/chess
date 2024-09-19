package pieces_rules;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;
import java.util.Collection;


public class Kingrules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row >= 1 && row <= 8 && column >= 1 && column <= 8) {
                    ChessPosition newPosition = new ChessPosition(row, column);
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece != null && piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPosition, board.getPiece(myPosition).getPieceType()));
                    }
                }
            }
        }
        return moves;
    }
}
