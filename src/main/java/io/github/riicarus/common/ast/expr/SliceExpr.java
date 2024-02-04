package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Array;
import io.github.riicarus.front.semantic.types.type.Basic;

/**
 * X[Index[0], Index[1]]
 *
 * @author Riicarus
 * @create 2024-1-15 21:20
 * @since 1.0.0
 */
public final class SliceExpr extends Expr {
    private Expr x;
    private Expr index1;
    private Expr index2;

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        Type xt = x.checkType(checker, null);
        Type it1 = index1 == null ? Basic.INT : index1.checkType(checker, null);
        Type it2 = index2 == null ? Basic.INT : index2.checkType(checker, null);

        if (xt instanceof Array && it1.equals(Basic.INT) && it2.equals(Basic.INT)) return xt;

        throw new IllegalStateException(String.format("Type error: need array[int:int], but get %s[%s:%s]", xt, it1, it2));
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Slice")
                .append(x.toTreeString(level + 1, prefix))
                .append(index1 == null ? "" : index1.toTreeString(level + 1, prefix))
                .append(index2 == null ? "" : index2.toTreeString(level + 1, prefix));
        return sb.toString();
    }

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    public Expr getIndex1() {
        return index1;
    }

    public void setIndex1(Expr index1) {
        this.index1 = index1;
    }

    public Expr getIndex2() {
        return index2;
    }

    public void setIndex2(Expr index2) {
        this.index2 = index2;
    }
}
