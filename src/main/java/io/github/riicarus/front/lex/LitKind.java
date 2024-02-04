package io.github.riicarus.front.lex;

import java.util.Map;

/**
 * Literal kind.
 *
 * @author Riicarus
 * @create 2024-1-15 20:49
 * @since 1.0.0
 */
public enum LitKind {

    INT(LexSymbol.INT_LIT.getName()),
    FLOAT(LexSymbol.FLOAT_LIT.getName()),
    CHAR(LexSymbol.CHAR_LIT.getName()),
    STRING(LexSymbol.STRING_LIT.getName()),
    TRUE(LexSymbol.TRUE.getName()),
    FALSE(LexSymbol.FALSE.getName()),
    NULL(LexSymbol.NULL.getName());

    private final String name;

    LitKind(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final Map<LexSymbol, LitKind> LIT_KIND_MAP = Map.of(
            LexSymbol.INT_LIT, INT,
            LexSymbol.FLOAT_LIT, FLOAT,
            LexSymbol.CHAR_LIT, CHAR,
            LexSymbol.STRING_LIT, STRING,
            LexSymbol.TRUE, TRUE,
            LexSymbol.FALSE, FALSE,
            LexSymbol.NULL, NULL
    );
}
