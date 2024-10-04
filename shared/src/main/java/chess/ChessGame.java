package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        Board.resetBoard();
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
        BLACK;

        @Override
        public String toString() {
            return this == WHITE? "white": "black";
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        if (Board.getPiece(startPosition) != null){

            Collection<ChessMove> all_moves = Board.getPiece(startPosition).pieceMoves(Board, startPosition);
            TeamColor color = Board.getPiece(startPosition).getTeamColor();
            ChessPiece piece = Board.getPiece(startPosition);
            for (ChessMove move : all_moves) {
                ChessPiece temporary = Board.getPiece(move.getEndPosition());
                Board.addPiece(startPosition, null);
                Board.addPiece(move.getEndPosition(), piece);
                if (!isInCheck(color))
                    validMoves.add(move);

                Board.addPiece(startPosition, piece);
                Board.addPiece(move.getEndPosition(), temporary);
            }
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
            Board.addPiece(move.getEndPosition(), getBoard().getPiece(move.getStartPosition()));

            if (move.getPromotionPiece() != null){
                ChessPiece it  = new ChessPiece(getBoard().getPiece(move.getStartPosition()).getTeamColor(),
                        move.getPromotionPiece());
                Board.addPiece(move.getEndPosition(), it);
            }

            Board.addPiece(move.getStartPosition(), null);
            if (getTeamTurn() == TeamColor.WHITE)
                setTeamTurn(TeamColor.BLACK);
            else
                setTeamTurn(TeamColor.WHITE);

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
                        && Board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    king = new ChessPosition(i, j);
                }
            }
        }
        for (int i = 1; i <= 8; i++){
            for(int j =1; j <=8; j++){
                ChessPiece enemy = Board.getPiece(new ChessPosition(i, j));
                if (enemy == null || enemy.getTeamColor() == teamColor)
                    continue;

                Collection<ChessMove> enemy_moves = enemy.pieceMoves(Board, new ChessPosition(i, j));
                for (ChessMove move: enemy_moves){
                    if (move.getEndPosition().equals(king)){
                        return true;
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
        for (int x = 1; x <= 8; x++){
            for (int y = 1; y <= 8; y++){
                ChessPiece piece = getBoard().getPiece(new ChessPosition(x,y));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(new ChessPosition(x, y));
                    if (!moves.isEmpty())
                        return false;
                }
            }
        }
        return true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(Board, chessGame.Board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, Board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", Board=" + Board +
                '}';
    }
}
