package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard Board;

    public ChessGame() {
        Board = new ChessBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> all_moves = Board.getPiece(startPosition).pieceMoves(Board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        TeamColor color = Board.getPiece(startPosition).getTeamColor();
        ChessPiece piece = Board.getPiece(startPosition);

        for (ChessMove move:  all_moves){
            ChessPiece temporary = Board.getPiece(move.getEndPosition());
            Board.addPiece(startPosition, null);
            Board.addPiece(move.getEndPosition(), piece);
            if(!isInCheck(color))
                validMoves.add(move);

            Board.addPiece(startPosition, piece);
            Board.addPiece(move.getEndPosition(), temporary);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.getStartPosition()) == null){
            throw new InvalidMoveException("No valid moves");
        }
        else if (validMoves(move.getStartPosition()).contains(move) &&
                Board.getPiece(move.getStartPosition()).getTeamColor() == teamTurn ) {
            Board.addPiece(move.getEndPosition(), Board.getPiece(move.getStartPosition()));
            Board.addPiece(move.getStartPosition(), null);
        }
        else
            throw new InvalidMoveException("invalid move: " + move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = null;
        for (int  i = 1; i <=8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (Board.getPiece(new ChessPosition(i, j)) != null &&
                        Board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING
                        && Board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamTurn) {
                    king = new ChessPosition(i, j);
                }
            }
        }
        for (int i = 1; i <= 8; i++){
            for(int j =1; j <=8; j++){
                ChessPiece enemy = Board.getPiece(new ChessPosition(i, j));

                if (enemy != null && enemy.getPieceType() != ChessPiece.PieceType.KING
                        && enemy.getTeamColor() != teamTurn) {
                    Collection<ChessMove> enemy_moves = enemy.pieceMoves(Board, new ChessPosition(i, j));
                    for (ChessMove move: enemy_moves){
                        if (move.getEndPosition() == king){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.Board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return Board;
    }
}
