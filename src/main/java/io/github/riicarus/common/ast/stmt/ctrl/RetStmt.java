package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;

/**
 * return Expr;
 *
 * @author Riicarus
 * @create 2024-1-19 11:54
 * @since 1.0.0
 */
public final class RetStmt extends Ctrl {
    private Expr retVal;

    public Expr getRetVal() {
        return retVal;
    }

    public void setRetVal(Expr retVal) {
        this.retVal = retVal;
    }
}
