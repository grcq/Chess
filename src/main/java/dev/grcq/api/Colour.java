package dev.grcq.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Colour {

    WHITE('w'),
    BLACK('b');

    private final char id;

    public static Colour fromId(char id) {
        return (id == 'w' ? WHITE : (id == 'b' ? BLACK : null));
    }

    public static Colour fromId(String id) {
        return fromId(id.toCharArray()[0]);
    }

}
