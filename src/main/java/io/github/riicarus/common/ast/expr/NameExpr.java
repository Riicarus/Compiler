package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;

/**
 * identifier
 *
 * @author Riicarus
 * @create 2024-1-15 21:15
 * @since 1.0.0
 */
public final class NameExpr extends Expr {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
