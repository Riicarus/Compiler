package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;

/**
 * SizeExpr:    "sizeof" "(" X ")"
 *
 * @author Riicarus
 * @create 2024-1-26 10:25
 * @since 1.0.0
 */
public class SizeExpr extends Expr {

    private Expr x;

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("SizeOf")
                .append(x.toTreeString(level + 1, prefix));
        return sb.toString();
    }
}
