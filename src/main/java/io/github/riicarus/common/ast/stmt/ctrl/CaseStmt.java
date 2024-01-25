package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;

/**
 * case Expr: CodeBlock
 *
 * @author Riicarus
 * @create 2024-1-19 11:55
 * @since 1.0.0
 */
public final class CaseStmt extends Ctrl {
    private Expr cond;
    private Stmt body;

    public Expr getCond() {
        return cond;
    }

    public void setCond(Expr cond) {
        this.cond = cond;
    }

    public Stmt getBody() {
        return body;
    }

    public void setBody(Stmt body) {
        this.body = body;
    }
}
