package pieces_rules;

import chess.*;


import java.util.Collection;
import java.util.HashSet;


public class Rookrules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        //right
        for (int i = 1; i <= 7; i++){
            int col = myPosition.getColumn() + i;
            if (col >= 1 && col <= 8 ){
                ChessPosition newPosition = new ChessPosition(myPosition.getRow(), col);
                ChessPiece piece = board.getPiece(newPosition);
                if (piece == null)
                    moves.add(new ChessMove(myPosition, newPosition, null));
                else{
                    if (piece.getTeamColor() != color)
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
            }
        }
        //left
        for (int i = -1; i >= -7; i--){
            int col = myPosition.getColumn() + i;
            if (col >= 1 && col <= 8 ){
                ChessPosition newPosition = new ChessPosition(myPosition.getRow(), col);
                ChessPiece piece = board.getPiece(newPosition);
                if (piece == null || piece.getTeamColor() != color)
                    moves.add(new ChessMove(myPosition, newPosition, null));
                else{
                    if (piece.getTeamColor() != color)
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
            }
        }
        //backward
        for (int i = -1; i >= -7; i--){
            int row = myPosition.getRow() + i;
            if (row >= 1 && row <= 8 ){
                ChessPosition newPosition = new ChessPosition(row, myPosition.getColumn());
                ChessPiece piece = board.getPiece(newPosition);
                if (piece == null || piece.getTeamColor() != color)
                    moves.add(new ChessMove(myPosition, newPosition, null));
                else{
                        if (piece.getTeamColor() != color)
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        break;
                    }
            }
        }
        //forward
        for (int i = 1; i <= 7; i++){
            int row = myPosition.getRow() + i;
            if (row >= 1 && row <= 8 ){
                ChessPosition newPosition = new ChessPosition(row, myPosition.getColumn());
                ChessPiece piece = board.getPiece(newPosition);
                if (piece == null)
                    moves.add(new ChessMove(myPosition, newPosition, null));
                else{
                    if (piece.getTeamColor() != color)
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    break;
                }
            }
        }

        return moves;
    }
}
