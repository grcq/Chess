package dev.grcq.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Square {

    A1("a1", 0, 0),
    A2("a2", 1, 0),
    A3("a3", 2, 0),
    A4("a4", 3, 0),
    A5("a5", 4, 0),
    A6("a6", 5, 0),
    A7("a7", 6, 0),
    A8("a8", 7, 0),

    B1("b1", 0, 1),
    B2("b2", 1, 1),
    B3("b3", 2, 1),
    B4("b4", 3, 1),
    B5("b5", 4, 1),
    B6("b6", 5, 1),
    B7("b7", 6, 1),
    B8("b8", 7, 1),

    C1("c1", 0, 2),
    C2("c2", 1, 2),
    C3("c3", 2, 2),
    C4("c4", 3, 2),
    C5("c5", 4, 2),
    C6("c6", 5, 2),
    C7("c7", 6, 2),
    C8("c8", 7, 2),

    D1("d1", 0, 3),
    D2("d2", 1, 3),
    D3("d3", 2, 3),
    D4("d4", 3, 3),
    D5("d5", 4, 3),
    D6("d6", 5, 3),
    D7("d7", 6, 3),
    D8("d8", 7, 3),

    E1("e1", 0, 4),
    E2("e2", 1, 4),
    E3("e3", 2, 4),
    E4("e4", 3, 4),
    E5("e5", 4, 4),
    E6("e6", 5, 4),
    E7("e7", 6, 4),
    E8("e8", 7, 4),

    F1("f1", 0, 5),
    F2("f2", 1, 5),
    F3("f3", 2, 5),
    F4("f4", 3, 5),
    F5("f5", 4, 5),
    F6("f6", 5, 5),
    F7("f7", 6, 5),
    F8("f8", 7, 5),

    G1("g1", 0, 6),
    G2("g2", 1, 6),
    G3("g3", 2, 6),
    G4("g4", 3, 6),
    G5("g5", 4, 6),
    G6("g6", 5, 6),
    G7("g7", 6, 6),
    G8("g8", 7, 6),

    H1("h1", 0, 7),
    H2("h2", 1, 7),
    H3("h3", 2, 7),
    H4("h4", 3, 7),
    H5("h5", 4, 7),
    H6("h6", 5, 7),
    H7("h7", 6, 7),
    H8("h8", 7, 7);

    private final String notation;
    private final int row;
    private final int file;

    public static Square fromNotation(String notation) {
        return valueOf(notation.toUpperCase());
    }

    public static Square fromRowAndFile(int row, int file) {
        if (row < 1 || row > 8 || file < 0 || file > 7) {
            return null;
        }

        char fileChar = (char) ('A' + file);
        String squareName = fileChar + String.valueOf(row);
        return valueOf(squareName);
    }

}
