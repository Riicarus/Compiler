package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;

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
}
