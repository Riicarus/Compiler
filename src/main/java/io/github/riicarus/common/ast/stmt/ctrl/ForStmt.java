package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.*;
import io.github.riicarus.common.ast.stmt.CodeBlock;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

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

    @Override
    public Type doCheckType(Checker checker, Type retType) {
        if (inits != null) inits.forEach(i -> ((ASTNode) i).checkType(checker, null));
        if (cond != null) {
            Type ct = cond.checkType(checker, null);
            if (!ct.equals(Basic.BOOL))
                throw new IllegalStateException("Type error: for condition should be bool");
        }
        if (updates != null) updates.forEach(u -> ((ASTNode) u).checkType(checker, null));

        if (body == null) throw new IllegalStateException("Missing body statement");

        if (body instanceof RetStmt ret) {
            Type retValType = ret.checkType(checker, null);
            if (!retValType.equals(retType))
                throw new IllegalStateException(String.format("Type error: return type need: %s, but get %s", retType, retValType));
        } else if (body instanceof CodeBlock cb) {
            checker.setLoopCnt(checker.getLoopCnt() + 1);
            cb.checkType(checker, retType);
        }
        else if (body instanceof ContinueStmt || body instanceof BreakStmt) throw new IllegalStateException("Illegal statement here");
        else body.checkType(checker, retType);

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

        sb.append(prefix).append(t).append(link).append("For")
                .append(cond == null ? "" : cond.toTreeString(level + 1, prefix))
                .append(body == null ? "" : body.toTreeString(level + 1, prefix));
        if (inits != null) inits.forEach(i -> sb.append(((ASTNode) i).toTreeString(level + 1, prefix)));
        if (updates != null) updates.forEach(u -> sb.append(((ASTNode) u).toTreeString(level + 1, prefix)));
        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

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
}
