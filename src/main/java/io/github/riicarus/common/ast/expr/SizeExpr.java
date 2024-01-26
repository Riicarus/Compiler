package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;

/**
 * SizeExpr:    "sizeof" "(" X ")"
 *
 * @author Riicarus
 * @create 2024-1-26 10:25
 * @since 1.0.0
 */
public class SizeExpr extends Expr {

    private Expr x;

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }
}
