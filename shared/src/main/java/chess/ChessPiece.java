package chess;

import pieces_rules.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return (switch (board.getPiece(myPosition).getPieceType()) {
            case KING -> new Kingrules();
            case QUEEN -> new Queenrules();
            case BISHOP -> new Bishoprules();
            case KNIGHT -> new Knightrules();
            case ROOK -> new Rookrules();
            case PAWN -> new Pawnrules();

        }).pieceMoves(board, myPosition);

    }

    @Override
    public String toString() {
        char c = switch (getPieceType()){
            case KING -> 'k';
            case QUEEN -> 'q';
            case BISHOP -> 'b';
            case KNIGHT -> 'n';
            case ROOK -> 'r';
            case PAWN -> 'p';
        };
        return switch(getTeamColor()){
            case WHITE -> String.valueOf(c).toUpperCase();
            case BLACK -> String.valueOf(c).toLowerCase();
        };
    }
}
