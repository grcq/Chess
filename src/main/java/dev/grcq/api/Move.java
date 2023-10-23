package dev.grcq.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Data
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class Move {

    private final String from;
    private final String to;

    private boolean capture = false;
    @Nullable
    private PieceType capturedType = null;

    private boolean promotion = false;
    @Nullable
    private PieceType promotionPiece = null;

    private boolean check = false;
    private boolean enPassant = false;

    private boolean castle = false;
    private char castleSide = '-';

    @Override
    public String toString() {
        return from + " -> " + to + "(c: " + capture + ", p: " +
                promotion + " [" + promotionPiece + "], ck: " + check + ")";
    }

}
