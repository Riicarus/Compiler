package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.stmt.decl.TypeDecl;

/**
 * X "." "(" Type ")"
 *
 * @author Riicarus
 * @create 2024-1-15 21:25
 * @since 1.0.0
 */
public final class CastExpr extends Expr {
    private Expr x;
    private TypeDecl type;

    public Expr getX() {
        return x;
    }

    public void setX(Expr x) {
        this.x = x;
    }

    public TypeDecl getType() {
        return type;
    }

    public void setType(TypeDecl type) {
        this.type = type;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Cast")
                .append(x.toTreeString(level + 1, prefix))
                .append(type.toTreeString(level + 1, prefix));

        return sb.toString();
    }
}
