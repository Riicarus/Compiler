package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;

import java.util.ArrayList;
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
    private final List<CaseStmt> cases = new ArrayList<>();
    private Stmt _default;

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
        this.cases.clear();
        this.cases.addAll(cases);
    }

    public Stmt getDefault() {
        return _default;
    }

    public void setDefault(Stmt _default) {
        this._default = _default;
    }
}