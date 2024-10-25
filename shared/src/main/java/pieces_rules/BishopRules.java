package pieces_rules;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.Collection;
import java.util.HashSet;

import static java.lang.Math.abs;


public class BishopRules implements PiecesRules {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();

        // up right
        outerLoop:
        for (int i = 1; i <= 7; i++) {
            for (int j = i; j <= 7; j++) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row >= 1 && row <= 8 && column >= 1 && column <= 8
                        && (abs(row- myPosition.getRow()) == abs(column-myPosition.getColumn()))) {
                    if (method(board, myPosition, moves, row, column)) break outerLoop;
                }
                else{
                    if (row < 1 || row > 8)
                        break;
                }
            }
        }
        // down right
        outerLoop:
        for (int i = -1; i >= -7; i--) {
            for (int j = 1; j <= 7; j++) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row >= 1 && row <= 8 && column >= 1 && column <= 8
                        && (abs(row- myPosition.getRow()) == abs(column-myPosition.getColumn()))
                        && (row != myPosition.getRow()) && (column != myPosition.getColumn())) {
                    if (method(board, myPosition, moves, row, column)) break outerLoop;
                }
                else{
                    if (row < 1 || row > 8)
                        break;
                }
            }

        }

        // down left
        outerLoop:
        for (int i = -1; i >= -7; i--) {
            for (int j = i; j >= -7; j--) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row >= 1 && row <= 8 && column >= 1 && column <= 8
                        && (abs(row- myPosition.getRow()) == abs(column-myPosition.getColumn()))
                        && (row != myPosition.getRow()) && (column != myPosition.getColumn())) {
                    if (method(board, myPosition, moves, row, column)) break outerLoop;
                }
                else{
                    if (row < 1 || row > 8)
                        break;
                }
            }

        }

        // up left
        outerLoop:
        for (int i = 1; i <= 7; i++) {
            for (int j = -1; j >= -7; j--) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row >= 1 && row <= 8 && column >= 1 && column <= 8
                        && (abs(row- myPosition.getRow()) == abs(column-myPosition.getColumn()))
                        && (row != myPosition.getRow()) && (column != myPosition.getColumn())) {
                    if (method(board, myPosition, moves, row, column)) break outerLoop;
                }
                else{
                    if (row < 1 || row > 8)
                        break;
                }
            }

        }
        return moves;
    }

    private boolean method(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row, int column) {
        ChessPosition newPosition = new ChessPosition(row, column);
        ChessPiece piece = board.getPiece(newPosition);
        if (piece == null)
            moves.add(new ChessMove(myPosition, newPosition, null));
        else {
            if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor())
                moves.add(new ChessMove(myPosition, newPosition, null));
            return true;
        }
        return false;
    }
}
