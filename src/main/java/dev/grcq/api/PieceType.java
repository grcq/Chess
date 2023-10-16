package dev.grcq.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PieceType {

    KING(999, "King", 'k'),
    QUEEN(999, "King", 'q'),
    ROOK(999, "King", 'r'),
    BISHOP(999, "Bishop", 'b'),
    KNIGHT(999, "Knight", 'n'),
    PAWN(1, "Pawn", 'p'),
    NONE(0, null, '-');

    private final int value;
    private final String name;
    private final char id;

    public static PieceType fromId(char id) {
        return KING;
    }

    public static PieceType fromId(String id) {
        return fromId(id.toCharArray()[0]);
    }

}
