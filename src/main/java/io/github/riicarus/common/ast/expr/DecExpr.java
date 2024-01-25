package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.SimpleStmt;

/**
 * X-- | --X
 *
 * @author Riicarus
 * @create 2024-1-25 16:36
 * @since 1.0.0
 */
public class DecExpr extends Expr implements SimpleStmt {

    protected Expr x;
    protected boolean preOrPost;

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    public boolean isPreOrPost() {
        return preOrPost;
    }

    public void setPreOrPost(boolean preOrPost) {
        this.preOrPost = preOrPost;
    }

}
