package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;

import java.util.ArrayList;
import java.util.List;

/**
 * "if" "(" Expr ")" NullableStmt ["elseif" "(" Expr ")" NullableStmt]... ["else" NullableStmt]
 *
 * @author Riicarus
 * @create 2024-1-17 15:39
 * @since 1.0.0
 */
public final class IfStmt extends Ctrl {
    private Expr cond;
    private Stmt then;
    private final List<ElseifStmt> elseIfs = new ArrayList<>();
    private Stmt _else;

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

    public List<ElseifStmt> getElseIfs() {
        return elseIfs;
    }

    public void setElseIfs(List<ElseifStmt> elseIfs) {
        this.elseIfs.clear();
        this.elseIfs.addAll(elseIfs);
    }

    public Stmt getElse() {
        return _else;
    }

    public void setElse(Stmt _else) {
        this._else = _else;
    }
}