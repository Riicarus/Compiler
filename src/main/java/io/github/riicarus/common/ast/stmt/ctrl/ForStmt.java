package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.*;

import java.util.List;

/**
 * "for" "(" ForInit ";" ForCond ";" ForUpdate ")" NullableStmt
 *
 * @author Riicarus
 * @create 2024-1-17 15:46
 * @since 1.0.0
 */
public final class ForStmt extends Ctrl {
    private List<SimpleStmt> inits;
    private Expr cond;
    private List<SimpleStmt> updates;
    private Stmt body;

    public List<SimpleStmt> getInits() {
        return inits;
    }

    public void setInits(List<SimpleStmt> inits) {
        this.inits = inits;
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
        this.updates = updates;
    }

    public Stmt getBody() {
        return body;
    }

    public void setBody(Stmt body) {
        this.body = body;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("For")
                .append(cond == null ? "" : cond.toTreeString(level + 1, prefix))
                .append(body == null ? "" : body.toTreeString(level + 1, prefix));
        if (inits != null) inits.forEach(i -> sb.append(((ASTNode) i).toTreeString(level + 1, prefix)));
        if (updates != null) updates.forEach(u -> sb.append(((ASTNode) u).toTreeString(level + 1, prefix)));
        return sb.toString();
    }
}
