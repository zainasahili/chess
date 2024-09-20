package pieces_rules;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;


public class Rookrules implements Piecesrules{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        //
        return moves;
    }
}
