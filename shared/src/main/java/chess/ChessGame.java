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
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
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
     * @return Set of valid moves for requested piece, or null   no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        if (board.getPiece(startPosition) != null){

            Collection<ChessMove> allMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
            TeamColor color = board.getPiece(startPosition).getTeamColor();
            ChessPiece piece = board.getPiece(startPosition);
            for (ChessMove move : allMoves) {
                ChessPiece temporary = board.getPiece(move.getEndPosition());
                board.addPiece(startPosition, null);
                board.addPiece(move.getEndPosition(), piece);
                if (!isInCheck(color)) {
                    validMoves.add(move);
                }

                board.addPiece(startPosition, piece);
                board.addPiece(move.getEndPosition(), temporary);
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
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves == null){
            throw new InvalidMoveException("No valid moves");
        }
        else if (validMoves.contains(move) &&
            board.getPiece(move.getStartPosition()).getTeamColor() == teamTurn ) {
            board.addPiece(move.getEndPosition(), getBoard().getPiece(move.getStartPosition()));

            if (move.getPromotionPiece() != null){
                ChessPiece it  = new ChessPiece(getBoard().getPiece(move.getStartPosition()).getTeamColor(),
                        move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), it);
            }

            board.addPiece(move.getStartPosition(), null);
            if (getTeamTurn() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            }
            else {
                setTeamTurn(TeamColor.WHITE);
            }

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
                if (board.getPiece(new ChessPosition(i, j)) != null &&
                        board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING
                        && board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    king = new ChessPosition(i, j);
                }
            }
        }
        for (int i = 1; i <= 8; i++){
            for(int j =1; j <=8; j++){
                ChessPiece enemy = board.getPiece(new ChessPosition(i, j));
                if (enemy == null || enemy.getTeamColor() == teamColor) {
                    continue;
                }

                Collection<ChessMove> enemyMoves = enemy.pieceMoves(board, new ChessPosition(i, j));
                for (ChessMove move: enemyMoves){
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
        return isInCheck(teamColor) && noMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noMoves(teamColor);

        }

    public boolean noMoves(TeamColor teamColor) {
        for (int x = 1; x <= 8; x++){
            for (int y = 1; y <= 8; y++){
                ChessPosition position  = new ChessPosition(x,y);
                ChessPiece piece = board.getPiece(position);
                Collection<ChessMove> moves = new HashSet<>();
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor){
                        moves = validMoves(position);
                    }
                    if (!moves.isEmpty()) {
                        return false;
                    }


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
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", Board=" + board +
                '}';
    }
}
