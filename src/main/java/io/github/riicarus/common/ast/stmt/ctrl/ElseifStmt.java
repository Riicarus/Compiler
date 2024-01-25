package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;

/**
 * "elseif" "(" Expr ")" NullableStmt
 *
 * @author Riicarus
 * @create 2024-1-17 15:44
 * @since 1.0.0
 */
public final class ElseifStmt extends Ctrl {
    private Expr cond;
    private Stmt then;

    public Expr getCond() {
        return cond;
    }

    public void setCond(Expr cond) {
        this.cond = cond;
    }

    public Stmt getThen() {
        return then;
    }

    public void setThen(Stmt then) {
        this.then = then;
    }
}
