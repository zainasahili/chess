package pieces_rules;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;


public class PawnRules implements PiecesRules {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        boolean blocked = false;


        int step = (color == ChessGame.TeamColor.WHITE ? 1 : -1);

        // if pawn is not at starting line or starting(1 step)
        int row = myPosition.getRow() + step;
        if (myPosition.getRow() >= 2 && myPosition.getRow() <= 8 &&
                myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8){
            ChessPosition newPosition = new ChessPosition(row, myPosition.getColumn());
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));
            else
                blocked = true;
        }

        // if pawn is at starting line(2 steps)
        moves.addAll(twoSteps(board, myPosition, moves, blocked, color, step));

        // if there's an enemy diagonally
        moves.addAll(captureEnemy(board, myPosition, step, row, color, moves));

        // if pawn is at then edge of the board
        if (row == 1 || row == 8)
            return promotion(moves, row);
        return moves;
    }

    public Collection<ChessMove> twoSteps(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves,
                                          boolean blocked, ChessGame.TeamColor color, int step){
        if (!blocked && ((color == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)
                || (color == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2))) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + (2 * step), myPosition.getColumn());
            if (board.getPiece(newPosition) == null)
                moves.add(new ChessMove(myPosition, newPosition, null));

        }
        return moves;
    }

    public Collection<ChessMove> captureEnemy(ChessBoard board, ChessPosition myPosition, int step, int row,
                                              ChessGame.TeamColor color, Collection<ChessMove> moves){
        int col_right = myPosition.getColumn() + step;
        int col_left = myPosition.getColumn() - step;
        ChessPosition newPosition = new ChessPosition(row, col_right);
        if (col_right >= 1 && col_right <= 8 && row >= 1 && row <= 8) {
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != color)
                moves.add(new ChessMove(myPosition, newPosition, null));
        }
        if (col_left >= 1 && col_left <= 8 && row >= 1 && row <= 8) {
            newPosition = new ChessPosition(row, col_left);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != color)
                moves.add(new ChessMove(myPosition, newPosition, null));
        }
        return moves;
    }
    public Collection<ChessMove> promotion(Collection<ChessMove> moves, int row){
            ArrayList<ChessMove> change = new ArrayList<>();
            for (ChessMove move: moves) {
                change.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
                change.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN));
                change.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK));
                change.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP));
            }
            return change;
    }
}
