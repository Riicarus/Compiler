package io.github.riicarus.common.ast.stmt.ctrl;

import io.github.riicarus.common.ast.Ctrl;
import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

/**
 * return Expr;
 *
 * @author Riicarus
 * @create 2024-1-19 11:54
 * @since 1.0.0
 */
public final class RetStmt extends Ctrl {
    private Expr retVal;

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        return retVal == null ? Basic.VOID : retVal.checkType(checker, null);
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Return")
                .append(retVal == null ? "" : retVal.toTreeString(level + 1, prefix));
        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public Expr getRetVal() {
        return retVal;
    }

    public void setRetVal(Expr retVal) {
        this.retVal = retVal;
    }
}
