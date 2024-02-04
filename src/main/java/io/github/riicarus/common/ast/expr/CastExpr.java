package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.stmt.decl.TypeDecl;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;
import io.github.riicarus.front.semantic.types.type.Basic;

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

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        Type xt = x.checkType(checker, null);
        Type t = type.checkType(checker, null);

        if (t instanceof Basic b && Basic.canCast(xt, t)) return t;

        throw new IllegalStateException(String.format("Type error: cannot cast %s to %s", xt, t));
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

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

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
}
