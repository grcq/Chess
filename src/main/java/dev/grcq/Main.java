package dev.grcq;

import dev.grcq.api.PieceType;
import dev.grcq.api.Square;
import dev.grcq.api.impl.Board;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        System.out.println(Arrays.asList(board.getMoves(PieceType.KNIGHT)));
    }

}