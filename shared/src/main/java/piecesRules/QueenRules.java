package piecesRules;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;


import java.util.Collection;
import java.util.HashSet;


public class QueenRules implements PiecesRules {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();

        moves.addAll(new BishopRules().pieceMoves(board, myPosition));
        moves.addAll(new RookRules().pieceMoves(board, myPosition));

        return moves;
    }
}
