package io.github.riicarus.front.lex;

/**
 * Literal kind.
 *
 * @author Riicarus
 * @create 2024-1-15 20:49
 * @since 1.0.0
 */
public enum LitKind {

    INT("int_lit"),
    FLOAT("float_lit"),
    CHAR("char_lit"),
    STRING("string_lit"),
    TRUE("true"),
    FALSE("false"),
    NULL("null");

    private final String name;

    LitKind(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
