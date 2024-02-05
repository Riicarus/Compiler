package io.github.riicarus.common.ast.expr;

import io.github.riicarus.common.ast.Expr;
import io.github.riicarus.common.ast.expr.lit.CompositeLit;
import io.github.riicarus.common.ast.stmt.decl.TypeDecl;
import io.github.riicarus.common.ast.stmt.decl.type.ArrayType;
import io.github.riicarus.front.semantic.Checker;
import io.github.riicarus.front.semantic.types.Type;

/**
 * @author Riicarus
 * @create 2024-1-19 11:51
 * @since 1.0.0
 */
public final class ArrExpr extends Expr {

    private ArrayType type;
    private CompositeLit lit;

    @Override
    public Type doCheckType(Checker checker, Type outerType) {
        if (lit != null) lit.checkType(checker, type.type());
        return type.checkType(checker, outerType);
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) sb.append("\r\n");

        sb.append(prefix).append(t).append(link).append("Array")
                .append(type.toTreeString(level + 1, prefix))
                .append(lit == null ? "" : lit.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    /* **************************************************************
     * Getters and Setters
     *************************************************************** */

    public ArrayType getType() {
        return type;
    }

    public void setBaseType(TypeDecl type) {
        this.type = new ArrayType();
        this.type.setEleType(type);
    }

    public int getSize() {
        return this.type.getSize();
    }

    public void setSize(int size) {
        this.type.setSize(size);
    }

    public CompositeLit getLit() {
        return lit;
    }

    public void setLit(CompositeLit lit) {
        this.lit = lit;
    }
}
