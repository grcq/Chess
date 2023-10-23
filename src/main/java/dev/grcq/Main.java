package dev.grcq;

import dev.grcq.ai.ChessAI;
import dev.grcq.api.Colour;
import dev.grcq.api.PieceType;
import dev.grcq.api.Square;
import dev.grcq.api.impl.Board;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();

        ChessAI ai = new ChessAI(board);

        System.out.println(ai.negamax(5, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

}