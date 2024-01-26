package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.SimpleStmt;

/**
 * X-- | --X
 *
 * @author Riicarus
 * @create 2024-1-25 16:36
 * @since 1.0.0
 */
public class DecExpr extends Expr implements SimpleStmt {

    protected Expr x;
    protected boolean preOrPost;

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    public boolean isPreOrPost() {
        return preOrPost;
    }

    public void setPreOrPost(boolean preOrPost) {
        this.preOrPost = preOrPost;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append(isPreOrPost() ? "Pre" : "Post").append("Decrease")
                .append(x.toTreeString(level + 1, prefix));
        return sb.toString();
    }
}
