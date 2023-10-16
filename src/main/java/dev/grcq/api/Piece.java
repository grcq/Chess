package dev.grcq.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Piece {

    private final PieceType type;
    private final Colour colour;

}
