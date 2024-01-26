package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;

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
}
