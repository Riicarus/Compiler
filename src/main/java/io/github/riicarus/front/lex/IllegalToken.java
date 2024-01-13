package io.github.riicarus.front.lex;

import io.github.riicarus.common.Position;

/**
 * IllegalToken
 *
 * @author Riicarus
 * @create 2024-1-13 11:43
 * @since 1.0.0
 */
public class IllegalToken extends Token {

    private final String detail;

    public IllegalToken(LexSymbol symbol, String lexeme, Position position, String detail) {
        super(symbol, lexeme, position);
        this.detail = detail;
    }

    @Override
    public String toString() {
        return super.toString() + "  " + detail;
    }
}
