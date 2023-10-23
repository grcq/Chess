package dev.grcq.ai;

import dev.grcq.api.Colour;
import dev.grcq.api.Move;
import dev.grcq.api.Piece;
import dev.grcq.api.Square;
import dev.grcq.api.impl.Board;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@RequiredArgsConstructor
public class ChessAI {

    private final Board board;
    private Colour playingAs = Colour.WHITE;

    private Move[] orderMoves(Move[] moves) {
        return Arrays.stream(moves).sorted((m1, m2) -> {
            if (m1.isCheck() && !m2.isCheck()) return -1;
            if (!m1.isCheck() && m2.isCheck()) return 1;

            if (m1.isCapture() && !m2.isCapture()) return -1;
            if (!m1.isCapture() && m2.isCapture()) return 1;

            return 0;
        }).toArray(Move[]::new);
    }

    public int negamax(int depth, int alpha, int beta) {
        if (board.isCheckmate()) return -Integer.MAX_VALUE;
        if (depth == 0) return -evaluate();
        int best = Integer.MIN_VALUE;

        Move[] moves = orderMoves(board.getMoves());
        System.out.println(moves[0]);
        for (int i = 0; i < moves.length; i++) {
            Move move = moves[i];

            board.move(move);
            best = Math.max(best, -negamax(depth - 1, -alpha, -beta));
            board.undo();

            alpha = Math.max(best, alpha);
            if (alpha >= beta) break;
        }

        return best;
    }

    private int evaluate() {
        int score = 0;

        for (Square square : Square.values()) {
            Piece piece = board.getPiece(square);
            if (piece == null) continue;

            int value = piece.getType().getValue();
            if (piece.getColour() != Colour.WHITE) value *= -1;

            score += value;
        }

        return score;
    }

}
