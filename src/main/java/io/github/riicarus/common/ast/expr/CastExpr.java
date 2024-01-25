package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.stmt.decl.type.TypeDecl;

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
}
