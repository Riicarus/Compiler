package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Array;
import io.github.riicarus.front.semantic.types.type.Basic;

/**
 * X[Index]
 *
 * @author Riicarus
 * @create 2024-1-15 21:18
 * @since 1.0.0
 */
public final class IndexExpr extends Expr {
    private Expr x;
    private Expr index;

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        Type xt = x.checkType(checker, null);
        Type it = index.checkType(checker, null);

        if (xt instanceof Array) {
            if (it.equals(Basic.INT))
                return xt.underlying();
            else throw new IllegalStateException(String.format("Type error: need int, but get %s", it));
        }

        throw new IllegalStateException(String.format("Type error: need array, but get %s", xt));
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Index")
                .append(x.toTreeString(level + 1, prefix))
                .append(index.toTreeString(level + 1, prefix));
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

    public Expr getIndex() {
        return index;
    }

    public void setIndex(Expr index) {
        this.index = index;
    }
}
