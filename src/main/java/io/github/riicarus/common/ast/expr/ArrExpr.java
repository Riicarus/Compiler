package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.stmt.decl.type.ArrayType;
import io.github.riicarus.common.ast.stmt.decl.type.TypeDecl;
import io.github.riicarus.common.ast.expr.lit.CompositeLit;

/**
 * @author Riicarus
 * @create 2024-1-19 11:51
 * @since 1.0.0
 */
public final class ArrExpr extends Expr {

    private final ArrayType type = new ArrayType();
    private Expr size;
    private CompositeLit lit;

    public ArrayType getType() {
        return type;
    }

    public void setBaseType(TypeDecl type) {
        this.type.setEleType(type);
    }

    public Expr getSize() {
        return size;
    }

    public void setSize(Expr size) {
        this.size = size;
    }

    public CompositeLit getLit() {
        return lit;
    }

    public void setLit(CompositeLit lit) {
        this.lit = lit;
    }
}
