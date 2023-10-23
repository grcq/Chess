package dev.grcq.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PieceType {

    KING(99999, "King", 'k'),
    QUEEN(900, "King", 'q'),
    ROOK(500, "King", 'r'),
    BISHOP(320, "Bishop", 'b'),
    KNIGHT(300, "Knight", 'n'),
    PAWN(100, "Pawn", 'p'),
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
