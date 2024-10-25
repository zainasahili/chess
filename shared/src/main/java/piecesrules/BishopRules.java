package piecesrules;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.Collection;
import java.util.HashSet;



public class BishopRules implements PiecesRules {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();

        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] direction: directions){
            int row = myPosition.getRow();
            int column = myPosition.getColumn();

            while (true){
                row += direction[0];
                column += direction[1];

                if (row < 1 || row > 8 || column < 1 || column > 8) {
                    break;
                }

                if (method(board, myPosition, moves, row, column)) {
                    break;
                }
            }

        }
        return moves;

    }

    private boolean method(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int column) {
        ChessPosition newPosition = new ChessPosition(row, column);
        ChessPiece piece = board.getPiece(newPosition);
        if (piece == null) {
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
        else {
            if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            return true;
        }
        return false;
    }
}


