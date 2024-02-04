package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

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
    private List<ElseifStmt> elseIfs;
    private Stmt _else;

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        Type ct = cond.checkType(checker, null);
        if (!ct.equals(Basic.BOOL))
            throw new IllegalStateException("Type error: elseif condition should be bool");
        if (then != null) then.checkType(checker, null);
        if (elseIfs != null) elseIfs.forEach(e -> e.checkType(checker, null));
        if (_else != null) _else.checkType(checker, null);

        return Basic.VOID;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("If")
                .append(cond.toTreeString(level + 1, prefix))
                .append(then == null ? "" : then.toTreeString(level + 1, prefix))
                .append(_else == null ? "" : _else.toTreeString(level + 1, prefix));
        if (elseIfs != null) elseIfs.forEach(e -> sb.append(e.toTreeString(level + 1, prefix)));
        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

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
        this.elseIfs = elseIfs;
    }

    public Stmt getElse() {
        return _else;
    }

    public void setElse(Stmt _else) {
        this._else = _else;
    }
}
