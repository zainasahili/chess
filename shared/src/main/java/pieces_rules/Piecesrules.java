package pieces_rules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface Piecesrules {

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

}