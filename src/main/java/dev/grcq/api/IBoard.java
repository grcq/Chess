package dev.grcq.api;

import org.jetbrains.annotations.Nullable;

public interface IBoard {

    boolean isCheckmate();

    boolean isDraw();

    @Nullable
    GameFinishedReason getGameOverReason();

    Move[] getMoves();

    boolean validateMove(Move move);

    @Nullable
    Piece getPiece(Square square);

    String toFEN();

    void move(Move move);

    Move undo(Move move);

    Move undo();

}
