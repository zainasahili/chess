package piecesRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface PiecesRules {

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

}