package pieces_rules;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.Collection;
import java.util.HashSet;

import static java.lang.Math.abs;


public class Bishoprules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();

        // up right
        outerloop:
        for (int i = 1; i <= 7; i++) {
            for (int j = i; j <= 7; j++) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row <= 8 && column <= 8
                        && (abs(row- myPosition.getRow()) == abs(column-myPosition.getColumn()))) {
                    ChessPosition newPosition = new ChessPosition(row, column);
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null)
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    else {
                        if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor())
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        break outerloop;
                    }
                }
                else{
                    if (row < 1 || row > 8)
                        break;
                }
            }

        }
        // down right
        outerloop:
        for (int i = -1; i >= -7; i--) {
            for (int j = 1; j <= 7; j++) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row >= 1 && row <= 8 && column >= 1 && column <= 8
                        && (abs(row- myPosition.getRow()) == abs(column-myPosition.getColumn()))
                        && (row != myPosition.getRow()) && (column != myPosition.getColumn())) {
                    ChessPosition newPosition = new ChessPosition(row, column);
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null)
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    else {
                        if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor())
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        break outerloop;

                    }
                }
                else{
                    if (row < 1 || row > 8)
                        break;
                }
            }

        }

        // down left
        outerloop:
        for (int i = -1; i >= -7; i--) {
            for (int j = i; j >= -7; j--) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row >= 1 && row <= 8 && column >= 1 && column <= 8
                        && (abs(row- myPosition.getRow()) == abs(column-myPosition.getColumn()))
                        && (row != myPosition.getRow()) && (column != myPosition.getColumn())) {
                    ChessPosition newPosition = new ChessPosition(row, column);
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null)
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    else {
                        if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor())
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        break outerloop;

                    }
                }
                else{
                    if (row < 1 || row > 8)
                        break;
                }
            }

        }

        // up left
        outerloop:
        for (int i = 1; i <= 7; i++) {

            for (int j = -1; j >= -7; j--) {
                int row = myPosition.getRow() + i;
                int column = myPosition.getColumn() + j;
                if (row >= 1 && row <= 8 && column >= 1 && column <= 8
                        && (abs(row- myPosition.getRow()) == abs(column-myPosition.getColumn()))
                        && (row != myPosition.getRow()) && (column != myPosition.getColumn())) {
                    ChessPosition newPosition = new ChessPosition(row, column);
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null)
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    else {
                        if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor())
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        break outerloop;

                    }
                }
                else{
                    if (row < 1 || row > 8)
                        break;
                }
            }

        }
        return moves;
    }
}
