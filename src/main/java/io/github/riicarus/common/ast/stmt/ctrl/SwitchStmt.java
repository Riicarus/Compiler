package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.common.ast.stmt.CodeBlock;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

import java.util.List;

/**
 * switch (Expr) case ... default ...
 *
 * @author Riicarus
 * @create 2024-1-19 11:55
 * @since 1.0.0
 */
public final class SwitchStmt extends Ctrl {
    private Expr x;
    private List<CaseStmt> cases;
    private Stmt _default;

    @Override
    public Type doCheckType(Checker checker, Type retType) {
        x.checkType(checker, null);
        if (cases != null) cases.forEach(c -> c.checkType(checker, null));

        if (_default == null) return Basic.VOID;

        if (_default instanceof RetStmt ret) {
            Type retValType = ret.checkType(checker, null);
            if (!retValType.equals(retType))
                throw new IllegalStateException(String.format("Type error: return type need: %s, but get %s", retType, retValType));
        } else if (_default instanceof CodeBlock cb) cb.checkType(checker, retType);
        else if (checker.getLoopCnt() == 0 && (_default instanceof ContinueStmt || _default instanceof BreakStmt)) throw new IllegalStateException("Illegal statement here");
        else _default.checkType(checker, retType);

        return Basic.VOID;
    }

    @Override
    public void checkStatement(Checker checker, Type retType) {
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Switch")
                .append(x.toTreeString(level + 1, prefix))
                .append(_default == null ? "" : _default.toTreeString(level + 1, prefix));
        if (cases != null) cases.forEach(c -> sb.append(c.toTreeString(level + 1, prefix)));
        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    public List<CaseStmt> getCases() {
        return cases;
    }

    public void setCases(List<CaseStmt> cases) {
        this.cases = cases;
    }

    public Stmt getDefault() {
        return _default;
    }

    public void setDefault(Stmt _default) {
        this._default = _default;
    }
}
