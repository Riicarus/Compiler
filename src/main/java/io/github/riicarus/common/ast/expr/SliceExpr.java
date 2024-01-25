package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;

/**
 * X[Index[0], Index[1]]
 *
 * @author Riicarus
 * @create 2024-1-15 21:20
 * @since 1.0.0
 */
public final class SliceExpr extends Expr {
    private Expr x;
    private Expr index1;
    private Expr index2;

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    public Expr getIndex1() {
        return index1;
    }

    public void setIndex1(Expr index1) {
        this.index1 = index1;
    }

    public Expr getIndex2() {
        return index2;
    }

    public void setIndex2(Expr index2) {
        this.index2 = index2;
    }
}
