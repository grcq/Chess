package dev.grcq.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Square {

    A1("a1", 0, 0);

    private final String notation;
    private final int row;
    private final int file;

    public static Square fromNotation(String notation) {
        return valueOf(notation.toUpperCase());
    }

}
