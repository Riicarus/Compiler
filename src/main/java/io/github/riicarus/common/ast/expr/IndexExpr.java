package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;

/**
 * X[Index]
 *
 * @author Riicarus
 * @create 2024-1-15 21:18
 * @since 1.0.0
 */
public final class IndexExpr extends Expr {
    private Expr x;
    private Expr index;

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    public Expr getIndex() {
        return index;
    }

    public void setIndex(Expr index) {
        this.index = index;
    }
}
