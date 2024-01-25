package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.common.ast.SimpleStmt;

import java.util.ArrayList;
import java.util.List;

/**
 * "for" "(" ForInit ";" ForCond ";" ForUpdate ")" NullableStmt
 *
 * @author Riicarus
 * @create 2024-1-17 15:46
 * @since 1.0.0
 */
public final class ForStmt extends Ctrl {
    private final List<SimpleStmt> inits = new ArrayList<>();
    private Expr cond;
    private final List<SimpleStmt> updates = new ArrayList<>();
    private Stmt body;

    public List<SimpleStmt> getInits() {
        return inits;
    }

    public void setInits(List<SimpleStmt> inits) {
        this.inits.clear();
        this.inits.addAll(inits);
    }

    public Expr getCond() {
        return cond;
    }

    public void setCond(Expr cond) {
        this.cond = cond;
    }

    public List<SimpleStmt> getUpdates() {
        return updates;
    }

    public void setUpdates(List<SimpleStmt> updates) {
        this.updates.clear();
        this.updates.addAll(updates);
    }

    public Stmt getBody() {
        return body;
    }

    public void setBody(Stmt body) {
        this.body = body;
    }
}
