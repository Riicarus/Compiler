package io.github.riicarus.front.lex;

import io.github.riicarus.common.Position;

import java.util.Objects;

/**
 * LexSymbol's parse result.
 * <p>
 * Token will be formatted as: &lt;code, name, lexeme&gt;,
 * only identifier and number tokens have lexeme attribute.
 *
 * @author Riicarus
 * @create 2024-1-12 13:07
 * @since 1.0.0
 */
public class Token {

    private final LexSymbol symbol;
    private final String lexeme;
    private final Position position;

    public Token(LexSymbol symbol, String lexeme, Position position) {
        this.symbol = symbol;
        this.lexeme = lexeme == null ? "" : lexeme;
        this.position = position;
    }

    public Token(LexSymbol symbol, Position position) {
        this.symbol = symbol;
        this.lexeme = "";
        this.position = position;
    }

    @Override
    public String toString() {
        // return position.toString() + "\t" + symbol.toString() + "\t" + lexeme;
        return String.format("%-20s  %-20s  %-32s", position, symbol, "\"" + lexeme + "\"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token token)) return false;
        return symbol == token.symbol && Objects.equals(lexeme, token.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, lexeme);
    }

    public LexSymbol getSymbol() {
        return symbol;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Position getPosition() {
        return position;
    }
}
