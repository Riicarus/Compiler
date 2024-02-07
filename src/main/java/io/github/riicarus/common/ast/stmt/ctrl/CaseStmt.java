package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.Stmt;
import io.github.riicarus.common.ast.stmt.CodeBlock;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

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

    @Override
    public Type doCheckType(Checker checker, Type retType) {
        Type ct = cond.checkType(checker, null);
        if (!ct.equals(Basic.BOOL))
            throw new IllegalStateException("Type error: case condition should be bool");

        if (body == null) throw new IllegalStateException("Missing body statement");

        if (body instanceof RetStmt ret) {
            Type retValType = ret.checkType(checker, null);
            if (!retValType.equals(retType))
                throw new IllegalStateException(String.format("Type error: return type need: %s, but get %s", retType, retValType));
        } else if (body instanceof CodeBlock cb) cb.checkType(checker, retType);
        else if (checker.getLoopCnt() == 0 && (body instanceof ContinueStmt || body instanceof BreakStmt)) throw new IllegalStateException("Illegal statement here");
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

        sb.append(prefix).append(t).append(link).append("Case")
                .append(cond.toTreeString(level + 1, prefix))
                .append(body == null ? "" : body.toTreeString(level + 1, prefix));
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

    public Stmt getBody() {
        return body;
    }

    public void setBody(Stmt body) {
        this.body = body;
    }
}
