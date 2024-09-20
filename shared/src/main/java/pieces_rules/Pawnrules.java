package pieces_rules;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;


public class Pawnrules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        boolean blocked = false;

        int step = (color == ChessGame.TeamColor.WHITE ? 1 : -1);

        // if pawn is not at starting line or starting(1 step)
        int row = myPosition.getRow() + step;
        if ((color == ChessGame.TeamColor.BLACK && myPosition.getRow() >= 1) ||
                (color == ChessGame.TeamColor.WHITE && myPosition.getRow() <= 8)) {
            ChessPosition newPosition = new ChessPosition(row, myPosition.getColumn());
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else
                blocked = true;
        }

        // if pawn is at starting line(2 steps)
        if (!blocked && ((color == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)
                || (color == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2))) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + (2 * step), myPosition.getColumn());
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));

        }

        // if there's an enemy diagonally
        int col_right = myPosition.getColumn() + step;
        int col_left = myPosition.getColumn() - step;
        ChessPosition newPosition = new ChessPosition(row, col_right);
        if (col_right >= 1 && col_right <= 8) {
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != color)
                moves.add(new ChessMove(myPosition, newPosition, null));
        }
        if (col_left >= 1 && col_left <= 8) {
            newPosition = new ChessPosition(row, col_left);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != color)
                moves.add(new ChessMove(myPosition, newPosition, null));
        }




        // if pawn is at then edge of the board

        if (row == 1 || row == 8){
            ArrayList<ChessMove> change = new ArrayList<>();
            for (ChessMove move: moves) {
                change.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                change.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN));
                change.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK));
                change.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP));
            }
            return change;
        }
        return moves;
    }
}
