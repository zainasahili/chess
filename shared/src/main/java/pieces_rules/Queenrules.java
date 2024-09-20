package pieces_rules;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


public class Queenrules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();

        moves.addAll(new Bishoprules().pieceMoves(board, myPosition));
        moves.addAll(new Rookrules().pieceMoves(board, myPosition));

        return moves;
    }
}
