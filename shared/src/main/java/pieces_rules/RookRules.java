package pieces_rules;

import chess.*;


import java.util.Collection;
import java.util.HashSet;


public class RookRules implements PiecesRules {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        //right
        for (int i = 1; i <= 7; i++){
            if (leftRight(board, myPosition, moves, color, i)) break;
        }
        //left
        for (int i = -1; i >= -7; i--){
            if (leftRight(board, myPosition, moves, color, i)) break;
        }
        //backward
        for (int i = -1; i >= -7; i--){
            if (forwardBackward(board, myPosition, moves, color, i)) break;
        }
        //forward
        for (int i = 1; i <= 7; i++){
            if (forwardBackward(board, myPosition, moves, color, i)) break;
        }

        return moves;
    }

    private boolean leftRight(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessGame.TeamColor color, int i) {
        int col = myPosition.getColumn() + i;
        if (col >= 1 && col <= 8 ){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), col);
            ChessPiece piece = board.getPiece(newPosition);
            if (piece == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else{
                if (piece.getTeamColor() != color)
                    moves.add(new ChessMove(myPosition, newPosition, null));
                return true;
            }
        }
        return false;
    }

    private boolean forwardBackward(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessGame.TeamColor color, int i) {
        int row = myPosition.getRow() + i;
        if (row >= 1 && row <= 8 ){
            ChessPosition newPosition = new ChessPosition(row, myPosition.getColumn());
            ChessPiece piece = board.getPiece(newPosition);
            if (piece == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else{
                if (piece.getTeamColor() != color)
                    moves.add(new ChessMove(myPosition, newPosition, null));
                return true;
            }
        }
        return false;
    }
}
