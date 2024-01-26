package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.expr.op.Operator;

/**
 * X Op Y, Y == null means unary operation
 *
 * @author Riicarus
 * @create 2024-1-16 14:32
 * @since 1.0.0
 */
public class Operation extends Expr {
    protected Expr x;
    protected Operator op;
    protected Expr y;

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    public Operator getOp() {
        return op;
    }

    public void setOp(Operator op) {
        this.op = op;
    }

    public Expr getY() {
        return y;
    }

    public void setY(Expr y) {
        this.y = y;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Operation  ").append(op.getOp())
                .append(x == null ? "" : x.toTreeString(level + 1, prefix))
                .append(y == null ? "" : y.toTreeString(level + 1, prefix));

        return sb.toString();
    }
}
