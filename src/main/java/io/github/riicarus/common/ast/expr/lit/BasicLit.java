package io.github.riicarus.common.ast.expr.lit;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.lex.LitKind;

/**
 * int_lit | float_lit | char_lit | string_lit | "true" | "false" | "null"
 *
 * @author Riicarus
 * @create 2024-1-15 21:15
 * @since 1.0.0
 */
public final class BasicLit extends Expr {
    private String value;
    private LitKind kind;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LitKind getKind() {
        return kind;
    }

    public void setKind(LitKind kind) {
        this.kind = kind;
    }
}
